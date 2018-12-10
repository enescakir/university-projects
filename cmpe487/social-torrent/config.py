import socket


def get_ip():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    try:
        s.connect(('10.255.255.255', 1))
        IP = s.getsockname()[0]
    except:
        IP = '127.0.0.1'
    finally:
        s.close()
    return IP

CHUNK_SIZE = 1480
FILE_PATH = './shared_files/'
SELF_IP = get_ip()
DISCOVERY_PORT = 5000  # TCP
CHUNK_PORT = 5001  # TCP
FILE_PORT = 5001  # UDP
ACK_PORT = 5001  # UDP

DEFAULT_WINDOW_SIZE = 250  # Window size
TRY_COUNT = 5  # Number of trials before giving up sending the packet
TOLERANCE = 3  # The limit of packet count tolerated before reaching window size
DRAINAGE = 0.01  # The timeout between the packets are processed from the queue (To simulate window size)

SUBNET = SELF_IP[:SELF_IP.rfind('.')]

MESSAGE_TYPES = {"request": 0, "response": 1}
