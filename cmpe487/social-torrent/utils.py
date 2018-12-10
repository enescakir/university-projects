import os
import socket


def send_packet(host, port, message):
    try:
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.settimeout(5)
            s.connect((host, port))
            s.send(message.encode('utf-8'))
            s.close()
    except Exception as ex:
        #print("Error while sending packet: " + message)
        #print(ex.__str__() + " " + str(port))
        pass


def clear():
    os.system('clear')


def enter_continue():
    print(change_style("\n\n\nEnter to continue...", 'bold'))
    tmp = input()
    clear()


def change_style(str, style):
    if style == "green":
        return "\033[92m{}\033[00m".format(str)
    elif style == "blue":
        return "\033[34m{}\033[00m".format(str)
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
