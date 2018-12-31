from models.simulation import Simulation
import config
# from config import SEED, SIMULATION_TIME, NUMBER_OF_SIMULATIONS, ALGORITHM
import random
import argparse
from utils import *
import os
from threading import Thread
import time
from graph import *


def print_value(name, value):
    print("\t{} {}".format(change_style(name + ":", 'green').rjust(44), change_style(value, 'bold')))


# Parse arguments
parser = argparse.ArgumentParser()
parser.add_argument('--time', help='simulation time', dest='time', type=int, default=config.SIMULATION_TIME)
parser.add_argument('--count', help='how many time simulation runs', dest='count', type=int,
                    default=config.NUMBER_OF_SIMULATIONS)
parser.add_argument('--seed', help='random seed', dest='seed', type=int, default=config.SEED)
parser.add_argument('--algorithm', help='cache replacement algorithm: LRU, LFU, RAND, MY', dest='algorithm',
                    default=config.ALGORITHM)
args = parser.parse_args()
config.SIMULATION_TIME = args.time
config.NUMBER_OF_SIMULATIONS = args.count
config.SEED = args.seed
config.ALGORITHM = args.algorithm

# Delete old logs
os.system("rm -f log_*.txt")

# Start simulations
start_time = time.time()
if config.NUMBER_OF_SIMULATIONS == 1:
    sim = Simulation(config.SEED, config.SIMULATION_TIME, 1)
    sim.start()
    draw_plot(sim.logger.logs, config.SIMULATION_TIME)
else:
    threads = []

    for i in range(config.NUMBER_OF_SIMULATIONS):
        sim = Simulation(random.randint(1, 9999), config.SIMULATION_TIME, i + 1)
        thread = Thread(target=sim.start)
        thread.start()
        threads.append(thread)

    for t in threads:
        t.join()

end_time = time.time()

os.system("clear")
print("\n\n" + change_style("=== PERFORMANCE RESULTS ===\n", "blue").rjust(64))
print_value("ALGORITHM", config.ALGORITHM)
print_value("SIMULATION TIME", config.SIMULATION_TIME)
print_value("SIMULATION COUNT", config.NUMBER_OF_SIMULATIONS)
print_value("REAL TIME", "{:.5f} seconds".format(end_time - start_time))
print_value("AVERAGE LATENCY", "{:.5f}".format(Simulation.performances['latency'] / config.NUMBER_OF_SIMULATIONS))
print_value("LOCAL HIT RATE SQ", "{:.5f}".format(Simulation.performances['p']['sq'] / config.NUMBER_OF_SIMULATIONS))
print_value("LOCAL HIT RATE HQ BASE",
            "{:.5f}".format(Simulation.performances['p']['hq']['base'] / config.NUMBER_OF_SIMULATIONS))
print_value("LOCAL HIT RATE HQ ENH. | BASE LH",
            "{:.5f}".format(Simulation.performances['p']['hq']['enh']['base_local_hit'] / config.NUMBER_OF_SIMULATIONS))
print_value("LOCAL HIT RATE HQ ENH. | BASE D2D",
            "{:.5f}".format(Simulation.performances['p']['hq']['enh']['base_d2d'] / config.NUMBER_OF_SIMULATIONS))
print("\n\n")
