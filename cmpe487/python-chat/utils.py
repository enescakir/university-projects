import os
import hashlib


class Message:
    def __init__(self, body, is_sent=False):
        self.body = body
        self.is_sent = is_sent
        self.is_read = False


class User:
    def __init__(self, ip, name, chipher=None):
        self.ip = ip
        self.name = name
        self.cipher = chipher
        self.unread = 0
        self.messages = []

    def add_message(self, message, is_sent):
        self.messages.append(Message(message, is_sent))
        if not is_sent:
            self.unread += 1

    def read_message(self):
        self.unread = 0
        for message in self.messages:
            message.is_read = True


def clear():
    os.system('clear')


def enter_continue():
    print(change_style("\n\n\nEnter to continue...", 'bold'))
    tmp = input()
    clear()


def change_style(str, style):
    if style == "green":
        return "\033[92m{}\033[00m".format(str)
    elif style == "header":
        return "\033[34m \033[01m{}\033[00m".format(str)
    elif style == "bold":
        return "\033[01m{}\033[00m".format(str)
    elif style == "red":
        return "\033[31m{}\033[00m".format(str)
    elif style == "error":
        return "\033[41m \033[37m{}\033[00m".format(str)
    elif style == "success":
        return "\033[42m \033[37m{}\033[00m".format(str)
    elif style == "underline":
        return "\033[4m{}\033[00m".format(str)
    elif style == "receiver":
        return "\033[01m\033[35m{}\033[00m".format(str)
    elif style == "sender":
        return "\033[01m\033[36m{}\033[00m".format(str)

    return str


def print_notification(str):
    print("\a \033[s \033[100F \033[2K \r {} {}  \033[u".format(change_style(" [!] ", "bold"),
                                                                change_style(str + " ", "success")), end="")


def print_error(str):
    print("\a \033[s \033[100F \033[2K \r {} {}  \033[u".format(change_style(" [x] ", "bold"),
                                                                change_style(str + " ", "error")), end="")


def print_header(header):
    print(change_style("\n\n=== " + header + " ===\n\n", 'header'))


def hash_MD5(str):
    m = hashlib.md5()
    m.update(str.encode('utf-8'))
    return m.hexdigest()
