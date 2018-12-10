import socket
from _thread import *
import threading
from utils import *
import random
import string
import select

class ChatServer:
    users = {}

    def __init__(self, username, discovery_port, message_port):
        self.username = username

        self.ip = socket.gethostbyname(socket.gethostname())
        self.network = self.ip[:self.ip.rfind('.')]
        self.discovery_port = discovery_port
        self.message_port = message_port
        self.buffer_size = 1024

    def send_packet(self, host, port, message, rediscover=False):
        try:
            with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
                s.settimeout(3)
                s.connect((host, port))
                s.send(message.encode('utf-8'))
                s.close()
        except:
            if rediscover:
                print_error("Message couldn't sent")
                self.inform_network()

    def send_discovery_response(self, host, name):
        message = ';'.join(['1', self.ip, self.username, host, name, ''])
        start_new_thread(self.send_packet, (host, self.discovery_port, message))

    def receive_discovery(self):
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
            s.bind((self.ip, self.discovery_port))
            s.listen()

            while True:
                conn, addr = s.accept()

                data = conn.recv(1024)
                if not data:
                    break

                discovery_packet = str(data.decode('utf-8'))

                try:
                    packet_type, source_ip, source_name, target_ip, target_name, *end = discovery_packet.split(";")
                    if packet_type == "1":
                        print_notification(source_name + " is online")
                        self.set_name(source_ip, source_name)
                    elif packet_type == "0":
                        print_notification(source_name + " open chat client")
                        self.set_name(source_ip, source_name)
                        self.send_discovery_response(source_ip, source_name)
                    else:
                        print_error("Unknown Discovery TCP Packet")
                except ValueError:
                    print_error("Unknown Discovery TCP Packet")
                conn.close()
            s.close()

    def receive_broadcast(self):
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.bind(('', self.discovery_port))
        s.setblocking(False)

        while True:
            data = select.select([s], [], [])

            if not data:
                break

            message = data[0][0].recv(self.buffer_size)

            if not message:
                break

            discovery_packet = str(message.decode('utf-8'))

            try:
                packet_type, source_ip, source_name, target_ip, target_name, *end = discovery_packet.split(";")
                if source_ip != self.ip:
                    if packet_type == "0":
                        print_notification(source_name + " open chat client")
                        self.set_cipher(source_ip, None)
                        self.set_name(source_ip, source_name)
                        self.send_discovery_response(source_ip, source_name)
                    else:
                        print_error("Unknown Discovery UDP Packet")
            except ValueError:
                print_error("Unknown Discovery UDP Packet")

        s.close()

    def listen_discovery(self):
        print(change_style("Discovering on port TCP " + change_style(str(self.discovery_port), 'bold'), 'green'))
        discovery_thread = threading.Thread(target=self.receive_discovery)
        discovery_thread.setDaemon(True)
        discovery_thread.start()

    def listen_broadcast_discovery(self):
        print(change_style("Discovering on port UDP " + change_style(str(self.discovery_port), 'bold'), 'green'))
        discovery_thread = threading.Thread(target=self.receive_broadcast)
        discovery_thread.setDaemon(True)
        discovery_thread.start()

    def inform_client(self, ip):
        message = ';'.join(['0', self.ip, self.username, ip, '', ''])
        self.send_packet(ip, self.discovery_port, message)

    def inform_network(self):
        print(change_style("Informing all device at same network...", 'green'))
        self.users = {}
        message = ';'.join(['0', self.ip, self.username, '', '', ''])
        sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        sock.bind(('', 0))
        sock.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
        sock.sendto(message.encode('utf-8'), ('<broadcast>', self.discovery_port))

    def discover_user(self, ip):
        start_new_thread(self.inform_client, (ip,))
        print(change_style('\n\nDiscovering ' + ip + '...', 'green'))

    def receive_message(self):
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
            s.bind((self.ip, self.message_port))
            s.listen()

            while True:
                conn, addr = s.accept()

                data = conn.recv(1024)
                if not data:
                    break

                message_packet = str(data.decode('utf-8'))

                try:
                    source_ip, source_cipher, body = message_packet.split(";")
                    target_cipher = self.get_cipher(source_ip)

                    if target_cipher is None or hash_MD5(target_cipher) == source_cipher:
                        user = self.get_user(source_ip)
                        user.cipher = source_cipher
                        user.add_message(body, False)
                        print_notification("New message from " + user.name)
                    else:
                        print_error("Not valid message from " + source_ip)
                except ValueError:
                    pass
                conn.close()

            s.close()

    def listen_message(self):
        print(change_style("Listening messages on port " + change_style(str(self.message_port), 'bold'), 'green'))
        message_thread = threading.Thread(target=self.receive_message)
        message_thread.setDaemon(True)
        message_thread.start()

    def select_chat(self):
        print_header("SELECT ONLINE USER")
        print("\t{0:s} {1:s}".format("0)".ljust(5), "Go back"))
        for ip, user in self.users.items():
            unread = ""
            if user.unread:
                unread = "(" + change_style(str(user.unread) + " unread messages", "green") + ")"
            print("\t{0:s} {1:s} {2:s}".format((ip[ip.rfind('.') + 1:] + ")").ljust(5), user.name, unread))

        id = input("\nEnter use ID: ")
        if id == "0":
            clear()
        else:
            self.show_chat(id)

    def show_chat(self, id):
        clear()
        target_ip = self.network + "." + id
        user = self.get_user(target_ip)
        if user is None:
            print_error("Please enter valid user ID")
            self.select_chat()
        else:
            print()
            if user.messages:
                for message in user.messages:
                    if message.is_sent:
                        print("{} {}".format((change_style(self.username + ": ", 'receiver')).ljust(25), message.body))
                    else:
                        notif = ""
                        if not message.is_read:
                            notif = change_style("*", "red")
                        print(
                            "{} {}".format((change_style(user.name + ": ", 'sender') + notif).ljust(25), message.body))
            else:
                print_error("You don't have any messages history with " + user.name)

            user.read_message()

            body = input("\n\nEnter your message [write 'exit' for go back]: \n> ")

            if body != "exit":
                if body:
                    user.messages.append(Message(body, True))
                    if user.cipher:
                        user.cipher = hash_MD5(user.cipher)
                    else:
                        user.cipher = hash_MD5(''.join(random.choices(string.ascii_uppercase + string.digits, k=10)))

                    message = ';'.join([self.ip, user.cipher, body])
                    start_new_thread(self.send_packet, (user.ip, self.message_port, message, True))
                self.show_chat(id)
            clear()

    def print_users(self):
        print_header("ONLINE USERS")
        print('-' * 46)
        print('| {0:s} | {1:s}|'.format("IP".ljust(20), "NAME".ljust(20)))
        print('-' * 46)
        for ip, user in self.users.items():
            print('| {0:s} | {1:s}|'.format(change_style(ip.ljust(20), 'bold'),
                                            change_style(user.name.ljust(20), 'green')))
        print('-' * 46)

    def print_profile(self):
        print_header("YOUR PROFILE")
        print('-' * 46)
        print('| {0:s} | {1:s}|'.format(change_style("Username".ljust(20), 'bold'),
                                        change_style(self.username.ljust(20), 'green')))
        print('-' * 46)
        print('| {0:s} | {1:s}|'.format(change_style("Local IP".ljust(20), 'bold'),
                                        change_style(self.ip.ljust(20), 'green')))
        print('-' * 46)
        print('| {0:s} | {1:s}|'.format(change_style("Local Network".ljust(20), 'bold'),
                                        change_style(self.network.ljust(20), 'green')))
        print('-' * 46)

    def get_users(self):
        # TODO: order users
        return self.users.items()
        # [print(key, " :: ", value) for (key, value) in sorted(self.users.items())]
        # return self.users.items().sort(key=lambda (k, v): (v, k))

    def get_name(self, ip):
        if ip in self.users:
            return self.users[ip].name

    def get_cipher(self, ip):
        if ip in self.users:
            return self.users[ip].cipher

    def get_user(self, ip):
        if ip in self.users:
            return self.users[ip]

    def get_total_unread(self):
        total = 0
        for ip, user in self.users.items():
            total += user.unread
        return total

    def get_unread(self, ip):
        if ip in self.users:
            return self.users[ip].name
        else:
            return 0

    def set_name(self, ip, name):
        if ip in self.users:
            self.users[ip].name = name
        else:
            self.users[ip] = User(ip, name)

    def set_cipher(self, ip, cipher):
        if ip in self.users:
            self.users[ip].cipher = cipher
        else:
            self.users[ip] = User(ip, 'Unknown', cipher)
