from config import *
from utils import *


class QuizClient():
    def __init__(self):
        self.username = None
        self.active_quiz = None
        self.score = 0
        self.available_quizzes = {}

    def start(self):
        self.broadcast_quiz()

    def broadcast_quiz(self, print=False):
        for i in range(1, 255):
            target_ip = SUBNET + "." + str(i)
            # if target_ip != SELF_IP:
            start_new_thread(self.send_discovery_packet, (target_ip, print))

    def send_discovery_packet(self, target_ip, print):
        message = "{}|{}".format(MESSAGE_TYPES["request"], SELF_IP)
        try:
            with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
                s.settimeout(5)
                s.connect((target_ip, DISCOVERY_PORT))
                s.send(message.encode('utf-8'))
                data = s.recv(1024)
                if data:
                    type, source, quiz_name = data.decode().split('|')
                    if int(type) == MESSAGE_TYPES["response"]:
                        print_notification("New quiz: {}".format(quiz_name))
                        self.available_quizzes[source] = quiz_name
                        if print:
                            clear()
                            select_option(self.available_quizzes.values(), prompt="Enter quiz ID",
                                          header="Enter a Quiz", is_active=False)

                s.close()
        except Exception as ex:
            # print("Error while sending packet: " + message)
            # print(ex.__str__() + " " + str(port))
            pass

    def enter(self, quiz_ip, username):
        self.username = username
        self.active_quiz = self.available_quizzes[quiz_ip]

        print("Entering: " + quiz_ip)
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.connect((quiz_ip, QUIZ_PORT))
            message = "{}|{}|{}".format(MESSAGE_TYPES["enter"], SELF_IP, username)
            s.send(message.encode('utf-8'))
            while True:
                data = s.recv(1024)
                if not data:
                    break

                type, *parts = data.decode().split("|")
                type = int(type)
                if type == MESSAGE_TYPES["error"]:
                    print(parts[0])
                    break
                elif type == MESSAGE_TYPES["success"]:
                    clear()
                    print("\n\n" + parts[0])
                    print_header("PLEASE WAIT TO START QUIZ")
                elif type == MESSAGE_TYPES["question"]:
                    clear()
                    number = parts[0]
                    body = parts[1]
                    options = parts[2:]
                    self.print_status()
                    print(change_style("\n\nQuestion {}: ".format(number), "question") + change_style(body, "bold"))
                    start_timer(QUESTION_TIME)
                    answer = select_option(options, prompt="Your answer", timeout=QUESTION_TIME)
                    if answer:
                        if int(answer) > 4 or int(answer) < 0:
                            print(change_style("\nInvalid answer\n", "red"))
                        else:
                            print("\nSubmitted answer is \"{}\"".format(options[int(answer) - 1]))

                        message = "{}|{}|{}".format(MESSAGE_TYPES["answer"], number, answer)
                        s.send(message.encode())
                    else:
                        print("\nTime is up. You got {} point".format(change_style("ZERO", "bold")))
                elif type == MESSAGE_TYPES["answer_response"]:
                    clear()
                    self.score = int(parts[0])
                    message = parts[1]
                    self.print_status()
                    print(change_style(message, "bold"))
                    print_header("PLEASE WAIT NEXT QUESTION")
                elif type == MESSAGE_TYPES["result"]:
                    clear()
                    print_header("SCOREBOARD")
                    for rank, result in enumerate(parts):
                        username, score = result.split(":")
                        if username == self.username:
                            print(change_style(
                                "{} {} {}".format((str(rank + 1) + ")").ljust(10), username.ljust(30),
                                                  score + " points"),
                                "sender"))
                        else:
                            print("{} {} {}".format((str(rank + 1) + ")").ljust(10), username.ljust(30),
                                                    score + " points"))

                    enter_continue()
                    break
                else:
                    print(type, parts)
            s.close()

    def print_status(self):
        print("\n\n")
        print("{}: {}".format(change_style("Username", "green").ljust(30), change_style(self.username, "bold")))
        print("{}: {}".format(change_style("Quiz Name", "green").ljust(30), change_style(self.active_quiz, "bold")))
        print("{}: {}".format(change_style("Current Score", "green").ljust(30), change_style(self.score, "bold")))
        print("\n\n")
