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


SELF_IP = get_ip()
SUBNET = SELF_IP[:SELF_IP.rfind('.')]

DISCOVERY_PORT = 5000  # TCP
QUIZ_PORT = 5001  # TCP

QUESTION_TIME = 10

MESSAGE_TYPES = {
    "request": 0,
    "response": 1,
    "enter": 2,
    "success": 3,
    "error": 4,
    "question": 5,
    "answer": 6,
    "answer_response": 7,
    "result": 8
}
