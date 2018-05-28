import os
import re
from indexer import Indexer
from query import Query
from query import QueryType

def make_bold(string):
    """
        Makes given string bold for terminal with escape characters
    """
    return "\033[1m{}\033[0m".format(string)

def command_help():
    """
        Prints help page of the application to terminal
    """
    print("\n\n\t\t\t\t\t" + make_bold("--- REUTERS SEARCH ENGINE ---"))
    print(make_bold("COMMANDS"))
    print("\t" + make_bold("exit") + "\t\t Exits from program")
    print("\t" + make_bold("help") + "\t\t Lists available commands")
    print("\t" + make_bold("index [FOLDER]") + "\t Indexes document positional inverted index. Default: reuters21578")
    print("\t" + make_bold("clear") + "\t\t Clear console screen")
    print("\t" + make_bold("remove") + "\t\t Removes inverted index files")
    print("\t" + make_bold("postings WORD") + "\t Returns postings of word")
    print("\n\t" + "** There is no special command for query processing.")
    print("\t" + "Inputs that aren't special command interpreted as query")
    print("\n\t" + make_bold("[QUERY_TYPE] YOUR_QUERY") + "\t Processes query based on given type.")
    print("\t\t\t\t If no type is given, it predicts query type")
    print("\t" + make_bold("Query Types"))
    print("\t\t" + make_bold(QueryType.CONJUNCTIVE) + " -> Conjunctive Query")
    print("\t\t" + make_bold(QueryType.PHRASE) + " -> Phrase Query")
    print("\t\t" + make_bold(QueryType.PROXIMITY) + " -> Proximity Query")
    print("\n\n\n")

def command_index(directory):
    """
        Indexes data that from given directory again 
    """
    global dictionary
    global index
    Indexer.remove_index()
    # Set default data directory
    if directory is None:
        directory = 'reuters21578'
    print('Indexing ' + directory + ' folder...')
    Indexer.create_index(directory=directory)
    dictionary, index = Indexer.get_index()
    print('Index created')

def command_remove():
    """
        Removes current index files
    """
    Indexer.remove_index()
    global dictionary
    global index
    dictionary = {}
    index = {}
    print('Index removed')

def command_postings(word, dictionary, index):
    """
        Returns postings of given word
    """
    postings = Indexer.get_postings(word, dictionary, index)
    print(postings)

def command_exit():
    """
        Exits from application
    """
    print("Goodbye...")
    exit(1)

def command_clear():
    """
        Clears terminal screen
    """
    os.system("clear")

####################################
########## APP START HERE ##########
####################################

# If the index isn't created create it
if not Indexer.is_indexed():
    command_index(None)
else:
    print('Data is already indexed')

dictionary, index = Indexer.get_index()

print("Type " + make_bold("help") + " for any documentation")
while True:
    # Get command from user and processes it
    command = input("query> ")
    postings_command = re.match(r'^postings\s(\w+)', command)
    index_command = re.match(r'^index\s?(\w+)?', command)
    if command == "exit":
        command_exit()
    elif index_command:
        command_index(index_command.group(1))
    elif command == "help":
        command_help()
    elif command == "clear":
        command_clear()
    elif command == "remove":
        command_remove()
    elif postings_command:
        command_postings(postings_command.group(1), dictionary, index)
    else:
        query = Query(command)
        result = query.run(dictionary, index)
        print(make_bold(str(len(result)) + ' documents are founded'))
        print(sorted(result))
