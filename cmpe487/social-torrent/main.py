import sys
import time

from fileClient import FileClient, start_download_queue
from fileServer import FileServer
from utils import *

fileServer = FileServer()
fileServer.start()
fileClient = FileClient(fileServer.send_shared_files, fileServer.new_file_downloaded)
fileClient.start()
start_download_queue(fileClient)

clear()
while True:
    print_header("AVAILABLE COMMANDS")

    commands = [
        "Share new file",
        "List active connections",
        "List available files",
        "List shared files",
        "Broadcast shared files",
        "Quit"
    ]

    for i, command in enumerate(commands):
        print("\t", change_style(str(i + 1) + ")", 'bold'), " ", command)

    option = input("\n" + change_style("Please enter your command", 'underline') + ": ")
    if option == "1":
        clear()
        print_header("SHARE NEW FILE")
        filepath = input("\n" + change_style("Enter absolute file path", 'underline') + ": ")
        if filepath:
            filename = os.path.basename(filepath)
            if fileServer.add_file(filepath):
                print("\n\n")
                print("\t" + change_style(filename, "bold") + change_style(" is added successfully", "green"))
            else:
                print("\n\n")
                print("\t" + change_style(filename, "bold") + change_style(" is not valid file", "red"))
        else:
            print("\n\n")
            print("\t" + change_style("Please enter a file path", "red"))
        enter_continue()
    elif option == "2":
        while True:
            clear()
            print_header("UPLOADS")
            print('-' * 89)
            print('| {0:s}| {1:s}| {2:s}| {3:s}|'.format("TARGET".ljust(20), "CHUNKS".ljust(20), "IN FLIGHT".ljust(20),
                                                         "WINDOW SIZE".ljust(20)))
            print('-' * 89)
            for target, connection in fileServer.active_connections.items():
                print('| {0:s}| {1:s}| {2:s}| {3:s}|'.format(target.ljust(20), str(len(connection.chunks)).ljust(20),
                                                             str(connection.in_flight).ljust(20),
                                                             str(connection.window_size).ljust(20)))
                print('-' * 89)

            print_header("DOWNLOADS")
            print('-' * 89)
            print('| {0:s}| {1:s}| {2:s}| {3:s}|'.format("FILE".ljust(20), "CHUNKS".ljust(20), "DOWNLOADED".ljust(20),
                                                         "STATUS".ljust(20)))
            print('-' * 89)
            for file in fileClient.available_files.values():
                if file.status != "discovered":
                    print('| {0:s}| {1:s}| {2:s}| {3:s}|'.format(file.name.ljust(20), str(file.chunk_size).ljust(20),
                                                                 str(len([1 for chunk in file.chunks if
                                                                          chunk.status == 'done'])).ljust(20),
                                                                 file.status.ljust(20)))
                    print('-' * 89)
            tmp = input(change_style("\n\nEmpty for refresh, enter 0 for exit: ", "bold"))
            if tmp == "0":
                clear()
                break
    elif option == "3":
        clear()
        print_header("AVAILABLE FILES")
        print('-' * 82)
        print('| {0:s}| {1:s}| {2:s}| {3:s}| {4:s}|'.format("ID".ljust(3), "NAME".ljust(30), "CHUNK SIZE".ljust(12),
                                                            "PEERS".ljust(10), "STATUS".ljust(16)))
        print('-' * 82)
        for id, file in enumerate(fileClient.available_files.values()):
            print('| {0:s}| {1:s}| {2:s}| {3:s}| {4:s}|'.format(str(id + 1).ljust(3),
                                                                change_style(file.name.ljust(30), 'bold'),
                                                                change_style(str(file.chunk_size).ljust(12), 'green'),
                                                                change_style(
                                                                    (str(len(file.peers)) + " peers").ljust(10),
                                                                    'green'),
                                                                change_style((file.status).ljust(16), 'green')
                                                                ))
        print('-' * 82)

        id = input("\nEnter file ID for downloading: ")
        if id is "":
            clear()
        else:
            selectedFile = list(fileClient.available_files.values())[int(id) - 1]
            fileClient.start_download(selectedFile.checksum)
            print(change_style("\n" + selectedFile.name, "bold") + change_style(" is started to downloading", "green"))
            enter_continue()
    elif option == "4":
        clear()
        print_header("SHARED FILES")
        print('-' * 83)
        print('| {0:s} | {1:s}| {2:s}|'.format("NAME".ljust(30), "CHUNK SIZE".ljust(12), "CHECKSUM".ljust(33)))
        print('-' * 83)
        for file in fileServer.shared_files.values():
            print('| {0:s} | {1:s}| {2:s}|'.format(change_style(file.name.ljust(30), 'bold'),
                                                   change_style(str(file.chunk_size).ljust(12), 'green'),
                                                   file.checksum.ljust(33)))
        print('-' * 83)
        enter_continue()
    elif option == "5":
        clear()
        print_header("BROADCAST SHARED FILES")
        fileServer.broadcast_shared_files()
        print(change_style("Broadcasting all shared files to network", "green"))
        time.sleep(1)
        enter_continue()
    elif option == "6":
        clear()
        print_notification("Good bye \n\n")
        os.system("pkill -9 \"python3 main.py\"")
        sys.exit(0)
    else:
        clear()
        print_error("Invalid option")
