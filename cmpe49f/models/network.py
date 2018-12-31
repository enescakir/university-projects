from simpy import PreemptiveResource, Interrupt
from config import NUMBER_OF_CHANNELS, CHANNEL_BANDWIDTH, INITIAL_FREQUENCY
import random


class Network:
    """ Handles channel allocation operations """

    def __init__(self, env):
        self.env = env
        self.channels = [PreemptiveResource(env, capacity=1) for _ in range(NUMBER_OF_CHANNELS)]

    def serve(self, user, cache, distance):
        """ Serves channel to user for transferring content """
        service_time = user.get_service_time(cache, distance)
        request = self.channels[user.channel_id].request(priority=user.get_priority(), preempt=True)
        request.user = user
        success = False
        yield request
        try:
            user.serving = True
            user.print("Start serving f_" + str(user.channel_id + 1))
            yield self.env.timeout(service_time)
            success = True
        except Interrupt:
            user.print("Drop serving f_" + str(user.channel_id + 1), 'red')
        finally:
            self.channels[user.channel_id].release(request)
            user.serving = False
            if success:
                user.print("End serving f_" + str(user.channel_id + 1))

        return success

    def get_current_type(self, ch):
        """ Returns type of current user that allocates given channel"""

        user = self.get_current_user(ch)
        if user:
            return user.type
        return None

    def get_current_user(self, ch):
        """ Returns current user that allocates given channel"""
        channel = self.channels[ch]
        if channel.users:
            return channel.users[0].user

        return None

    def get_idle_channel(self):
        """ Returns first idle channel """
        idles = []
        for ch in range(NUMBER_OF_CHANNELS):
            if self.channels[ch].count == 0:
                idles.append(ch)

        if idles:
            return random.choice(idles)

        return None

    @staticmethod
    def get_channel_frequency(ch):
        """ Calculates frequency of given channel """
        return INITIAL_FREQUENCY + (int(ch) - 1) * CHANNEL_BANDWIDTH
