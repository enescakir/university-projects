import sys
import time
from server import *

# MAIN APPLICATION
clear()
print_header("WELCOME TO P2P PYTHON CHAT APP")

# Kill old chat applications
os.system('pkill -f "Python app.py"')

# Get username
username = input("\n" + change_style("Enter your username", 'underline') + ": ")

# Start chat server
clear()
server = ChatServer(username, 5000, 5001)
server.print_profile()
enter_continue()
server.listen_discovery()
server.inform_network()
server.listen_message()
time.sleep(2)
clear()

while True:
    print_header("AVAILABLE COMMANDS")

    commands = ["List online users", "Send message", "Show my profile", "Discover user", "Quit"]

    total_unread = server.get_total_unread()

    if total_unread:
        commands[1] = "Send message (" + change_style(str(total_unread) + " unread messages", "green") + ")"

    for i, command in enumerate(commands):
        print("\t", change_style(str(i + 1) + ")", 'bold'), " ", command)

    option = input("\n" + change_style("Please enter your command", 'underline') + ": ")
    if option == "1":
        clear()
        server.print_users()
        enter_continue()
    elif option == "2":
        clear()
        server.select_chat()
    elif option == "3":
        clear()
        server.print_profile()
        enter_continue()
    elif option == "4":
        clear()
        print_header("DISCOVER NEW USERS")
        ip = input("\n" + change_style("Enter user IP for discovery", 'underline') + ": ")
        if ip:
            server.discover_user(ip)
            enter_continue()
            clear()
        else:
            clear()
            print_error("Invalid IP address")
    elif option == "5":
        clear()
        print_notification("Good bye " + server.username + " \n\n")
        sys.exit(0)
    else:
        clear()
        print_error("Invalid option")
