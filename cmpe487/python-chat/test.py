import socket
import sys
import random
from utils import *

TARGET_HOST = socket.gethostbyname(socket.gethostname())
if len(sys.argv) > 1:
    TARGET_HOST = sys.argv[1]

TARGET_NETWORK = TARGET_HOST[:TARGET_HOST.rfind('.')]
NAMES = ['Enes', 'Emirhan', 'Deniz', 'Mehmet', 'Ali', 'Ceren', 'Buse', 'Büşra', 'Ahmet', 'Veli']
MESSAGES = ['Naber', 'Nasılsın?', 'İyi misin?', 'Selamlar', 'Hava nasıl?', 'Güzel', 'Tebrikler']
DISCOVER_PORT = 5000
MESSAGE_PORT = 5001


def send_packet(host, port, message):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        try:
            s.connect((host, port))
            s.send(message.encode('utf-8'))
            s.close()
        except:
            print("Connection error")


def get_random_name():
    return random.choice(NAMES)


def get_random_message():
    return random.choice(MESSAGES)


def get_random_ip():
    return TARGET_NETWORK + "." + str(random.randint(1, 255))


def send_discover_request(ip=None):
    source_ip = ip if ip else get_random_ip()
    print(change_style("Sent discovery request from " + source_ip, "green"))
    packet = "0;{};{};{};;".format(source_ip, get_random_name(), TARGET_HOST)
    send_packet(TARGET_HOST, DISCOVER_PORT, packet)
    return source_ip


def send_discover_response(ip=None):
    source_ip = ip if ip else get_random_ip()
    print(change_style("Sent discovery response from " + source_ip, "green"))
    packet = "1;{};{};{};{};".format(source_ip, get_random_name(), TARGET_HOST, get_random_name())
    send_packet(TARGET_HOST, DISCOVER_PORT, packet)
    return source_ip


def send_message(raw_cipher, hash_count=0, ip=None):
    source_ip = ip if ip else get_random_ip()
    cipher = raw_cipher
    for x in range(hash_count):
        cipher = hash_MD5(cipher)

    print(change_style("Sent message from " + source_ip + " with '" + cipher + "' cipher", "green"))

    packet = "{};{};{}".format(source_ip, cipher, get_random_message())
    send_packet(TARGET_HOST, MESSAGE_PORT, packet)

    return cipher


if __name__ == "__main__":
    clear()
    print_header("TESTING " + TARGET_HOST + " CHAT SERVER")
    ip = send_discover_request()
    send_discover_response(ip)
    send_message("test", 0, ip)
    send_message("test", 1, ip)
    send_message("test", 2, ip)

    ip = send_discover_request()
    ip = send_discover_request()
    print("\n\n\n")
