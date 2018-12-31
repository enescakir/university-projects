from enum import Enum
from models.cache import Cache
from models.network import Network
from config import CHANNEL_BANDWIDTH, CACHE_CAPACITY, DEVICE_RADIUS, NOISE, LAMBDA_USERS_PPP, RADIUS, \
    NUMBER_OF_CACHE_TRY
from collections import Counter
import math
import random
import sys
from utils import *
import numpy as np
import config


class Point:
    """ Represents cartesian location of the user """

    def __init__(self, x, y):
        self.x = x
        self.y = y

    def distance(self, point):
        """ Calculates distance between self and given point """
        return math.sqrt((self.x - point.x) ** 2 + (self.y - point.y) ** 2)


class UserType(Enum):
    """ Represents type of user """
    PRIMARY = "PU"
    SECONDARY = "SU"


class User:
    """ Represents user in simulation """

    def __init__(self, id, type, point, env, contents):
        self.id = id
        self.type = type
        self.position = point
        self.env = env
        self.caches = []
        self.distances = []
        self.channel_id = None
        self.serving = False
        if type == UserType.SECONDARY:
            self.fill_cache(contents)
            # print([str(x) for x in sorted(self.caches, key=lambda k: k.id)])

    def __str__(self):
        return str(self.type.value) + '_' + str(self.id)

    def calculate_distances(self, users):
        """ Calculates distance with other devices and sort them low to high """
        for user in users:
            self.distances.append({"id": user.id, "distance": self.position.distance(user.position)})
        self.distances = sorted(self.distances, key=lambda k: k['distance'])

    def fill_cache(self, contents):
        """ Fills cache storage of device with random content based on their popularity """
        cache = Cache.get_random(contents)
        count = NUMBER_OF_CACHE_TRY
        while self.get_remaining_size() > cache.size:
            if not self.is_cached(cache):
                self.caches.append(cache)
                self.used_cache(cache)
                count = NUMBER_OF_CACHE_TRY
            cache = Cache.get_random(contents)
            while self.get_remaining_size() < cache.size and count > 0:
                cache = Cache.get_random(contents)
                count -= 1

    def get_channel_power(self, distance):
        """ Calculates channel power that depends on distance"""
        if self.type == UserType.PRIMARY:
            return 4.7e13 / (
                    (4 * math.pi * Network.get_channel_frequency(self.channel_id) * distance) ** 2)
        elif self.type == UserType.SECONDARY:
            return 2.6e13 / (
                    (4 * math.pi * Network.get_channel_frequency(self.channel_id) * distance) ** 2)

    def get_channel_capacity(self, distance):
        """ Calculates channel capacity with its channel power"""
        return CHANNEL_BANDWIDTH * math.log2(1 + (self.get_channel_power(distance) / (CHANNEL_BANDWIDTH * NOISE)))

    def is_cached(self, cache):
        """ Checks is the device has that content's cache """
        for cached in self.caches:
            if cache.id == cached.id and cache.type == cached.type:
                return True
        return False

    def used_cache(self, cache):
        """ Changes use frequency and access time of the used cache """
        for cached in self.caches:
            if cache.id == cached.id and cache.type == cached.type:
                cached.LFU += 1
                cached.LRU = self.env.now

    def get_service_time(self, cache, distance):
        """ Calculates service time of given cache file """
        return cache.size / self.get_channel_capacity(distance)

    def get_used_size(self):
        """ Returns used size of cache storage """
        return sum([cache.size for cache in self.caches])

    def get_remaining_size(self):
        """ Returns remaining size of cache storage """
        return CACHE_CAPACITY - self.get_used_size()

    def get_priority(self):
        """ Returns priority of user for drop it """
        if self.type == UserType.PRIMARY:
            return 1
        elif self.type == UserType.SECONDARY:
            return 2

    def look_other_users(self, cache, users):
        for user_dic in self.distances:
            if user_dic['id'] != self.id and users[user_dic['id']].is_cached(cache):
                return users[user_dic['id']]

    def store_cache(self, cache, users, contents):
        self.open_free_space(cache, users, contents)
        self.caches.append(cache)
        self.print("Store cache " + str(cache), 'blue')

    def open_free_space(self, new_cache, users, contents):
        while new_cache.size > self.get_remaining_size():
            self.delete_cache(new_cache, users, contents)

    def delete_cache(self, new_cache, users, contents):
        cache = None
        if config.ALGORITHM == "LRU":
            lr = {"index": 0, "time": self.env.now}
            for i, cache in enumerate(self.caches):
                if cache.LRU <= lr["time"]:
                    lr["index"] = i
                    lr["time"] = cache.LRU
            cache = self.caches.pop(lr["index"])
        elif config.ALGORITHM == "LFU":
            lf = {"index": 0, "frequency": sys.maxsize}
            for i, cache in enumerate(self.caches):
                if cache.LFU <= lf["frequency"]:
                    lf["index"] = i
                    lf["frequency"] = cache.LFU
            cache = self.caches.pop(lf["index"])
        elif config.ALGORITHM == "RAND":
            cache = self.caches.pop(random.choice(range(len(self.caches))))
        elif config.ALGORITHM == "MY":
            cache_counts = Counter()
            neighbors = [distance['id'] for distance in self.distances if
                         (distance['distance'] <= DEVICE_RADIUS and distance['id'] != self.id)]
            for id in neighbors:
                cache_count = [str(cache) for cache in users[id].caches]
                cache_counts.update(cache_count)

            for cache in self.caches:
                count = cache_counts.get(str(cache))
                cache.weight = (count if count is not None else 0) / contents[cache.id - 1].popularity

            cache_to_look = [cache for cache in self.caches if cache.type == new_cache.type]
            if not cache_to_look:
                cache_to_look = self.caches

            cache_to_look.sort(key=lambda x: x.weight, reverse=True)
            cache = cache_to_look[0]
            self.caches.remove(cache)

        self.print("Remove cache " + str(cache), 'red')

    def print(self, message, style=None):
        """ Prints user action to console """
        if config.NUMBER_OF_SIMULATIONS == 1:
            print(
                "{}{} {}".format(change_style(("  [" + "{:.10f}".format(self.env.now) + "]").ljust(20), 'time'),
                                 change_style((" " + str(self) + " ").ljust(9), 'user'),
                                 change_style(message, style)))

    @staticmethod
    def get_random(users):
        """ Return random secondary user """
        return random.choice(users)

    @staticmethod
    def generate(env, contents):
        """ Generates random secondary user with PPP """
        users = []
        n = np.random.poisson(LAMBDA_USERS_PPP * np.pi * RADIUS ** 2)
        u_1 = np.random.uniform(0.0, 1.0, n)  # generate n uniformly distributed points
        radii = np.zeros(n)  # the radial coordinate of the points
        angle = np.zeros(n)  # the angular coordinate of the points
        for i in range(n):
            radii[i] = RADIUS * (np.sqrt(u_1[i]))
        u_2 = np.random.uniform(0.0, 1.0, n)  # generate another n uniformly distributed points
        for i in range(n):
            angle[i] = 2 * np.pi * u_2[i]
        for i in range(n):
            x = radii[i] * np.cos(angle[i])
            y = radii[i] * np.sin(angle[i])
            user = User(i, UserType.SECONDARY, Point(x, y), env, contents)
            users.append(user)

        for user in users:
            user.calculate_distances(users)

        return users
