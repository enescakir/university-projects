import simpy
from models.user import UserType
from models.user import User
from models.user import Point
from models.cache import CacheType
from models.cache import Cache
from models.content import Content
from models.network import Network
from models.log import Logger
from threading import Lock, Semaphore
import random
from utils import *
from config import NUMBER_OF_CHANNELS, PRIMARY_USER_DISTANCE, PROBABILITY_HQ, LAMBDA_PRIMARY_USER, \
    LAMBDA_SECONDARY_USER, BASE_LATENCY, ENC_LATENCY


class Simulation:
    """ Manages simulation's environment """
    performances = {"latency": 0, "p": {"sq": 0, "hq": {"base": 0, "enh": {"base_local_hit": 0, "base_d2d": 0}}}}
    lock = Lock()
    semaphore = Semaphore(20)

    def __init__(self, seed, time, id):
        self.id = id
        self.seed = seed
        self.time = time
        self.network = None
        self.env = None
        self.logger = Logger(seed)
        self.users = []
        self.contents = []

    def handle_primary_user(self, user):
        start_time = self.env.now
        # Choose a random frequency channel
        channel = random.choice(range(NUMBER_OF_CHANNELS))

        # Get user that using this channel at the moment
        current_user = self.network.get_current_user(channel)
        if current_user is None:
            # Channel is idle, serve PU
            user.channel_id = channel
            content = Content.get_random(self.contents)
            yield from self.network.serve(user, Cache(content.id, CacheType.BASE, 25e6),
                                          PRIMARY_USER_DISTANCE)
            self.logger.new("SERVED", False, "PU", "BASE", start_time, self.env.now, None, None, channel)
        elif current_user.type == UserType.PRIMARY:
            # Channel is used by another PU, block coming PU
            user.print("Block f_" + str(channel + 1), 'red')
            self.logger.new("BLOCKED", False, "PU", "BASE", start_time, self.env.now, None, None, None)
        elif current_user.type == UserType.SECONDARY:
            # Channel is used by SU, drop SU, serve PU
            user.channel_id = channel
            content = Content.get_random(self.contents)
            user.print("Preempt f_" + str(channel + 1), 'blue')
            yield from self.network.serve(user, Cache(content.id, CacheType.BASE, 25e6),
                                          PRIMARY_USER_DISTANCE)
            self.logger.new("SERVED", False, "PU", "BASE", start_time, self.env.now, None, None, channel)

    def handle_secondary_user(self, user):
        start_time = self.env.now
        # Check if it's already in system
        if user.serving:
            user.print("Blocked already serving", 'red')
            self.logger.new("BLOCKED", False, "SU", "BASE", start_time, self.env.now, None, None, None)
            return
        # Get idle channel for SU
        idle_channel = self.network.get_idle_channel()

        if idle_channel is not None:
            # We found idle channel
            content = Content.get_random(self.contents)
            user.channel_id = idle_channel

            success = True
            blocked = False
            is_hq = random.random() < PROBABILITY_HQ
            prev_event_id = None

            # Create cache instance from random content
            cache_base = Cache(content.id, CacheType.BASE, content.base)
            if user.is_cached(cache_base):
                # Has base layer of content at our device
                user.print("Local hit " + str(cache_base), 'green')
                user.used_cache(cache_base)  # Change LFU and LRU values
                prev_event_id = self.logger.new("LOCAL HIT", is_hq, "SU", "BASE", start_time, self.env.now, user.id,
                                                user.id,
                                                None)
            else:
                # Looks for base layer to other users
                source = user.look_other_users(cache_base, self.users)
                if source is None:
                    self.logger.new("BLOCKED", is_hq, "SU", "BASE", start_time, self.env.now, None, None, None)
                    user.print("Not find cache " + str(cache_base), 'red')
                    success = False
                    blocked = True
                else:
                    user.print("Found " + str(cache_base) + " at " + str(source), 'blue')
                    success = yield from self.network.serve(user, cache_base,
                                                            user.position.distance(source.position))
                    prev_event_id = self.logger.new("SERVED" if success else "DROPPED", is_hq, "SU", "BASE",
                                                    start_time,
                                                    self.env.now,
                                                    source.id, user.id, user.channel_id)
                    if success:
                        user.store_cache(cache_base, self.users, self.contents)
            if is_hq:
                # Look for enh layer after base is finished
                start_time = self.env.now
                if success:
                    # Download base layer successfully
                    cache_enhancement = Cache(content.id, CacheType.ENHANCEMENT, content.enhancement)
                    if user.is_cached(cache_enhancement):
                        # Has enh layer of content at our device
                        user.print("Local hit " + str(cache_enhancement), 'green')
                        self.logger.new("LOCAL HIT", is_hq, "SU", "ENH", start_time, self.env.now, user.id, user.id,
                                        None,
                                        prev_event_id)
                    else:
                        source = user.look_other_users(cache_enhancement, self.users)
                        if source is None:
                            self.logger.new("BLOCKED", is_hq, "SU", "ENH", start_time, self.env.now, None, None,
                                            None)
                            user.print("Not find cache " + str(cache_enhancement), 'red')
                        else:
                            user.print("Found " + str(cache_enhancement) + " at " + str(source), 'blue')

                            success = yield from self.network.serve(user, cache_enhancement,
                                                                    user.position.distance(source.position))
                            self.logger.new("SERVED" if success else "DROPPED", is_hq, "SU", "ENH", start_time,
                                            self.env.now,
                                            source.id, user.id, user.channel_id, prev_event_id)
                            user.store_cache(cache_enhancement, self.users, self.contents)

                else:
                    # Couldn't download base layer successfully
                    self.logger.new("BLOCKED" if blocked else "DROPPED", is_hq, "SU", "ENH", start_time,
                                    self.env.now,
                                    None, user.id, user.channel_id, prev_event_id)
        else:
            # We couldn't find idle channel, block coming SU
            user.print("No idle channel", 'red')
            self.logger.new("BLOCKED", False, "SU", "BASE", start_time, self.env.now, None, None, None)

    def request_content(self, user):
        if user.type == UserType.PRIMARY:
            yield from self.handle_primary_user(user)
        elif user.type == UserType.SECONDARY:
            yield from self.handle_secondary_user(user)

    def calculate_performance(self):
        t_all = 0
        count = {"base": 0, "enh": 0}
        count_sq = {"total": 0, "local_hit": 0}
        count_hq = {"base": {"total": 0, "local_hit": 0, "d2d": 0}, "enh": {"base_local_hit": 0, "base_d2d": 0}}
        hq_base_local_hits = []
        hq_base_d2d = []
        for log in self.logger.logs:
            if log.user == "SU":
                if log.req_type == "SQ":
                    count_sq["total"] += 1
                    if log.ev_type == "SERVED":
                        t_all += (log.end - log.start)
                        count["base"] += 1
                    elif log.ev_type == "LOCAL HIT":
                        t_all += BASE_LATENCY
                        count["base"] += 1
                        count_sq["local_hit"] += 1
                elif log.req_type == "HQ" and log.layer == "BASE":
                    count_hq['base']['total'] += 1
                    if log.ev_type == "SERVED":
                        t_all += (log.end - log.start)
                        count["enh"] += 1
                        count_hq['base']['d2d'] += 1
                        hq_base_d2d.append(log.id)
                    elif log.ev_type == "LOCAL HIT":
                        t_all += BASE_LATENCY
                        count["enh"] += 1
                        count_hq['base']['local_hit'] += 1
                        hq_base_local_hits.append(log.id)
                elif log.req_type == "HQ" and log.layer == "ENH":
                    if log.ev_type == "SERVED":
                        t_all += (log.end - log.start)
                    elif log.ev_type == "LOCAL HIT":
                        t_all += ENC_LATENCY
                        if log.prev_ev_id in hq_base_local_hits:
                            count_hq['enh']['base_local_hit'] += 1
                        elif log.prev_ev_id in hq_base_d2d:
                            count_hq['enh']['base_d2d'] += 1

        latency = t_all / (count["base"] + count["enh"])
        p_loc_sq = count_sq["local_hit"] / count_sq["total"]
        p_loc_hq_base = count_hq['base']['local_hit'] / count_hq['base']['total']
        p_loc_hq_enh_base_loc = count_hq['enh']['base_local_hit'] / count_hq['base']['local_hit']
        p_loc_hq_enh_base_d2d = count_hq['enh']['base_d2d'] / count_hq['base']['d2d']

        return {"latency": latency, "p": {"sq": p_loc_sq, "hq": {"base": p_loc_hq_base,
                                                                 "enh": {"base_local_hit": p_loc_hq_enh_base_loc,
                                                                         "base_d2d": p_loc_hq_enh_base_d2d}}}}

    def arrival_process(self, arrival_rate, type):
        i = 0
        while True:
            interval = random.expovariate(arrival_rate)
            yield self.env.timeout(interval)
            if type == UserType.PRIMARY:
                user = User(i, UserType.PRIMARY, Point(0, 0), self.env, self.contents)
                i += 1
            else:
                user = User.get_random(self.users)  # Get a random user from generated secondary users

            user.print("Arrived", 'green')
            self.env.process(self.request_content(user))

    def start(self):
        """ Runs simulation with given seed"""
        Simulation.semaphore.acquire()
        self.env = simpy.Environment()

        random.seed(self.seed)

        print(change_style("[Simulation #{}]".format(self.id), 'blue') + change_style(" Generating contents", "info"))
        self.contents = Content.generate()

        print(change_style("[Simulation #{}]".format(self.id), 'blue') + change_style(
            " Generating secondary users and fill up their caches",
            "info"))
        self.users = User.generate(self.env, self.contents)

        self.network = Network(self.env)
        # Create PU arrivals
        self.env.process(self.arrival_process(LAMBDA_PRIMARY_USER, UserType.PRIMARY))

        # Create SU arrivals
        self.env.process(self.arrival_process(LAMBDA_SECONDARY_USER, UserType.SECONDARY))

        print(change_style("[Simulation #{}]".format(self.id), 'blue') + change_style(" Starting", "info"))
        self.env.run(until=self.time)
        print(change_style("[Simulation #{}]".format(self.id), 'blue') + change_style(" Ending", "info"))

        self.logger.save()
        Simulation.semaphore.release()

        performance = self.calculate_performance()
        Simulation.lock.acquire()
        Simulation.performances['latency'] += performance['latency']
        Simulation.performances['p']['sq'] += performance['p']['sq']
        Simulation.performances['p']['hq']['base'] += performance['p']['hq']['base']
        Simulation.performances['p']['hq']['enh']['base_local_hit'] += performance['p']['hq']['enh']['base_local_hit']
        Simulation.performances['p']['hq']['enh']['base_d2d'] += performance['p']['hq']['enh']['base_d2d']
        Simulation.lock.release()
