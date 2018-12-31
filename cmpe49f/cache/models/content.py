from config import ZIPF_PARAMETER, NUMBER_OF_CONTENTS, LAMBDA_BASE_SIZE, LAMBDA_ENC_SIZE
import numpy as np
import random


class Content:
    """ Represents content in simulation """

    def __init__(self, id, base, enhancement):
        self.id = id
        self.base = base
        self.enhancement = enhancement
        self.popularity = self.calculate_popularity()

    def calculate_popularity(self):
        return (1 / (self.id ** ZIPF_PARAMETER)) / sum(
            [(1 / (n ** ZIPF_PARAMETER)) for n in range(1, NUMBER_OF_CONTENTS + 1)])

    @staticmethod
    def get_random(contents):
        """ Returns random content for secondary user request """
        return np.random.choice(contents, p=[content.popularity for content in contents])

    @staticmethod
    def generate():
        """ Generates initial contents """
        contents = []
        for i in range(1, NUMBER_OF_CONTENTS + 1):
            base = random.expovariate(1 / LAMBDA_BASE_SIZE)
            enhancement = random.expovariate(1 / LAMBDA_ENC_SIZE)
            contents.append(Content(i, int(base), int(enhancement)))
        return contents
