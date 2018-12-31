import os
import socket
import sys
import select
import time
from _thread import *


def send_packet(host, port, message):
    try:
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.settimeout(5)
            s.connect((host, port))
            s.send(message.encode('utf-8'))
            s.close()
    except Exception as ex:
        # print("Error while sending packet: " + message)
        # print(ex.__str__() + " " + str(port))
        pass


def clear():
    os.system('clear')


def select_option(commands, prompt="Please enter your command", is_active=True, header=None, timeout=None):
    if header:
        print_header(header)

    for i, command in enumerate(commands):
        print("\t", change_style(str(i + 1) + ")", 'bold'), " ", command)

    if is_active:
        if timeout:
            print("\n" + change_style(prompt, 'underline') + ": ")
            return timed_input(timeout)
        else:
            return input("\n" + change_style(prompt, 'underline') + ": ")
    else:
        print("\n" + change_style(prompt, 'underline') + ": ")


def init_timer(count):
    count *= 4
    while count >= 0:
        print_timer(count)
        time.sleep(0.25)
        count -= 1


def start_timer(count):
    start_new_thread(init_timer, (count,))


def timed_input(timeout=10):
    i, o, e = select.select([sys.stdin], [], [], timeout)
    if i:
        return sys.stdin.readline().strip()

    return None


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
    elif style == "question":
        return "\033[01m\033[36m{}\033[00m".format(str)
    return str


def print_notification(str):
    print("\a \033[s \033[100F \033[2K \r {} {}  \033[u".format(change_style(" [!] ", "bold"),
                                                                change_style(str + " ", "success")), end="")


def print_timer(count):
    if count % 4 == 1:
        sym = "/"
    elif count % 4 == 2:
        sym = "–"
    elif count % 4 == 3:
        sym = "\\"
    else:
        sym = "|"

    os.system("tput sc;")
    print("\033[100F \033[2K \r{} {}".format(
        change_style("Remaining Time: ", "underlined").rjust(50),
        change_style(str(count // 4).rjust(2) + " seconds " + sym, "bold")), end="")
    os.system("tput rc;")
    # os.system("echo `tput ll`;")
    # print("\033[s \033[100F \033[2K \r{} {}\033[u".format(
    #     change_style("Remaining Time: ", "underlined").rjust(50),
    #     change_style(str(count // 4).rjust(2) + " seconds " + sym, "bold")), end="")


def print_error(str):
    print("\a \033[s \033[100F \033[2K \r {} {}  \033[u".format(change_style(" [x] ", "bold"),
                                                                change_style(str + " ", "error")), end="")


def print_header(header):
    print(change_style("\n\n=== " + header + " ===\n\n", 'header'))
