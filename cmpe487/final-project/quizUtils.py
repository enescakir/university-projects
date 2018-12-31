from threading import Thread
from utils import change_style, print_header
from config import MESSAGE_TYPES


class Quiz:
    def __init__(self, name):
        self.name = name
        self.questions = []

    def import_file(self, filename):
        file = open(filename)
        for line in file:
            body, correct_answer, *options = line.strip().split("|")
            self.new_question(body, options, correct_answer)

    def new_question(self, body, options, correct_answer):
        self.questions.append(Question(body, options, correct_answer))

    def add_question(self, question):
        self.questions.append(question)

    def get_correct_answer(self, number):
        return self.questions[number - 1].options[self.get_correct_option(number) - 1]

    def get_correct_option(self, number):
        return int(self.questions[number - 1].correct_answer)

    def print(self):
        print_header("QUIZ: " + self.name)
        for number, question in enumerate(self.questions):
            print()
            print(change_style("Question {}: ".format(number + 1), "question") + change_style(question.body, "bold"))
            for i, option in enumerate(question.options):
                print(change_style(str(i + 1) + ") ", "bold") + option)


class Question:
    def __init__(self, body, options, correct_answer):
        self.body = body
        self.options = options
        self.correct_answer = correct_answer

    @staticmethod
    def from_input(order):
        print(change_style("\n\nQuestion {}".format(order), "bold"))
        body = input(change_style("Question body", 'underline') + ": ")
        options = []
        for i in range(4):
            option = input(change_style("Option {}".format(i + 1), 'underline') + ": ")
            options.append(option)
        correct_answer = int(input(change_style("Correct answer", 'underline') + ": "))
        return Question(body, options, correct_answer)


class Participant(Thread):
    def __init__(self, username, ip, quiz, score, connection):
        Thread.__init__(self)
        self.username = username
        self.ip = ip
        self.score = score
        self.connection = connection
        self.quiz = quiz

    def run(self):
        while True:
            try:
                data = self.connection.recv(1024)
                if not data:
                    break
                type, *parts = data.decode().split('|')

                if int(type) == MESSAGE_TYPES['answer']:
                    question_number = int(parts[0])
                    answer = int(parts[1])
                    correct_answer = self.quiz.get_correct_option(question_number)
                    if int(correct_answer) == int(answer):
                        print(change_style("{} answered correct".format(self.username), "green"))
                        self.score += 100
                        message = "{}|{}|{}".format(MESSAGE_TYPES["answer_response"], self.score,
                                                    "Congratulations!!! Your answer is correct.")
                    else:
                        print(change_style("{} answered wrong".format(self.username), "red"))
                        message = "{}|{}|{}".format(MESSAGE_TYPES["answer_response"], self.score,
                                                    "LOSERRRR!!! Your answer is false. Correct answer is \"{}\"".format(
                                                        self.quiz.get_correct_answer(question_number)))
                    self.send_message(message)
                else:
                    print(type, parts)
            except:
                pass

    def send_message(self, message):
        self.connection.send(message.encode())

    def close(self):
        self.connection.close()
