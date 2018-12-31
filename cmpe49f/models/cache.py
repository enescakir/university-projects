from enum import Enum
from models.content import Content
import random


class CacheType(Enum):
    """ Represents type of cache """
    BASE = "Base"
    ENHANCEMENT = "Enh"


class Cache:
    """ Represents content cache at device cache storage """

    def __init__(self, id, type, size):
        self.id = id
        self.type = type
        self.size = size
        self.LFU = 0
        self.LRU = 0
        self.weight = 0

    def __str__(self):
        return "Cache" + "_" + str(self.type.value) + '_' + str(self.id)

    @staticmethod
    def get_random(contents):
        """ Return random cache for initial filling"""
        content = Content.get_random(contents)
        if random.random() < .5:
            return Cache(content.id, CacheType.ENHANCEMENT, content.enhancement)
        else:
            return Cache(content.id, CacheType.BASE, content.base)
