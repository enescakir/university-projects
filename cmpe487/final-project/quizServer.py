import threading
from config import *
from utils import *
from quizUtils import *
import time


class QuizServer:

    def __init__(self, quiz):
        self.quiz = quiz
        self.is_started = False
        self.is_ended = False
        self.current_question = None
        self.participants = {}
        self.discovery_socket = None
        self.quiz_socket = None

    def listen(self):
        self.listen_discovery_request()
        self.listen_quiz_request()

    def start(self):
        self.is_started = True
        for number, question in enumerate(self.quiz.questions):
            clear()
            print_header("Sending question to participants")
            print("Question {}: {}\n".format(number + 1, question.body))
            self.current_question = number
            message = "{}|{}|{}".format(MESSAGE_TYPES["question"], number + 1, question.body)
            for option in question.options:
                message += "|" + option

            self.broadcast_message(message)
            print_header("Waiting for answers from participants")
            start_timer(QUESTION_TIME + 2)
            time.sleep(QUESTION_TIME + 2)
            clear()
            self.print_scores()
            if number < len(self.quiz.questions) - 1:
                enter_continue()
        self.end()

    def end(self):
        clear()
        message = "{}".format(MESSAGE_TYPES["result"])
        sorted_participants = sorted(self.participants.values(), key=lambda x: x.score, reverse=True)
        for p in sorted_participants:
            message += "|{}:{}".format(p.username, p.score)
        self.broadcast_message(message)
        self.print_scores()
        for p in self.participants.values():
            p.close()
        self.discovery_socket.close()
        self.quiz_socket.close()
        print_header("END OF THE QUIZ")
        enter_continue()

    def print_scores(self):
        sorted_participants = sorted(self.participants.values(), key=lambda x: x.score, reverse=True)
        print_header("SCORES")
        for rank, p in enumerate(sorted_participants):
            print("{}) {} {}".format(change_style(rank + 1, "bold").ljust(10),
                                     change_style(p.username, "green").ljust(30),
                                     change_style(str(p.score) + " points", "receiver")))

        pass

    def receive_discovery_request(self):
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            self.discovery_socket = s
            s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
            s.bind((SELF_IP, DISCOVERY_PORT))
            s.listen()
            while True:
                try:
                    conn, addr = s.accept()

                    data = conn.recv(1024)
                    if not data:
                        break

                    message = str(data.decode('utf-8'))
                    type, source = message.split('|')
                    if int(type) == MESSAGE_TYPES["request"]:
                        response = "{}|{}|{}".format(MESSAGE_TYPES["response"], SELF_IP, self.quiz.name)
                        conn.send(response.encode())
                    conn.close()
                except:
                    pass

    def receive_quiz_request(self):
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            self.quiz_socket = s
            s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
            s.bind((SELF_IP, QUIZ_PORT))
            s.listen()
            while True:
                try:
                    conn, addr = s.accept()
                    data = conn.recv(1024)
                    if not data:
                        break

                    message = str(data.decode('utf-8'))
                    type, source, username = message.split('|')
                    if int(type) == MESSAGE_TYPES['enter']:
                        if self.is_started:
                            conn.send("{}|{}".format(MESSAGE_TYPES["error"], "Quiz is already started").encode())
                        else:
                            participant = Participant(username, source, self.quiz, 0, conn)
                            participant.start()
                            self.participants[source] = participant
                            participant.send_message("{}|{}".format(MESSAGE_TYPES["success"],
                                                                    "You entered quiz \"{}\" successfully".format(
                                                                        self.quiz.name)))

                            clear()
                            print_header("QUIZ: " + self.quiz.name)
                            print(change_style("QUIZ IP: " + change_style(SELF_IP + "\n", 'bold'), 'green'))
                            self.print_participants()
                            print("\n\nEnter for start quiz")
                except:
                    pass

    def print_participants(self):
        if self.participants:
            print(change_style("{} PARTICIPANTS".format(len(self.participants)), 'bold'))
            for participant in self.participants.values():
                print(change_style(participant.username, "receiver") + " - " + participant.ip)
        else:
            print(change_style("NO PARTICIPANTS", 'bold'))

    def listen_discovery_request(self):
        discovery_thread = threading.Thread(target=self.receive_discovery_request)
        discovery_thread.setDaemon(True)
        discovery_thread.start()

    def listen_quiz_request(self):
        quiz_thread = threading.Thread(target=self.receive_quiz_request)
        quiz_thread.setDaemon(True)
        quiz_thread.start()

    def clear_dead_threads(self):
        deads = [p.ip for p in self.participants.values() if not p.is_alive()]
        for dead in deads:
            del self.participants[dead]

    def broadcast_message(self, message):
        self.clear_dead_threads()
        for participant in self.participants.values():
            participant.send_message(message)
