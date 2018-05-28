import os, re, pickle
from tokenizer import Tokenizer

class Indexer:
    """
        Handles inverted index operations
    """
    DICTIONARY_NAME = 'dictionary.txt' # Name of the dictionary file
    INDEX_NAME = 'inverted_index.txt'  # Name of the inverted index file
    POSTING_ID = 1 # Starting ID for posting lists

    @classmethod
    def read_files(self, directory=None):
        """
            Returns read documents from data directory
        """
        # If no directory is given, set it to current directory
        directory = os.getcwd() if directory is None else directory
        filenames = os.listdir(directory)
        # Get all file with .sgm extension
        filenames = [filename for filename in filenames if filename.endswith(".sgm")]
        filenames.sort()
        documents = []
        # Extract documents from each file
        for filename in filenames:
            raw_data = open(os.path.join(directory, filename), "r", encoding="latin-1").read()
            documents += self.extract_documents(raw_data)
        return documents

    @classmethod
    def extract_documents(self, raw_data):
        """
            Extracts documents from raw string
        """
        # Some news don't have body or title
        # return re.findall(r'<REUTERS.*?NEWID=\"(?P<id>\d+)\">.*?<TITLE>(?P<title>.*?)</TITLE>.*?<BODY>(?P<body>.*?)</BODY>.*?</REUTERS>', raw_data, re.DOTALL)
        documents = []
        # Seperate each document
        raw_documents = raw_data.split('</REUTERS>')
        # Extract information from each raw document string
        for raw_document in raw_documents:
            doc_id = re.match(r'.+?NEWID=\"(?P<id>\d+)\">.+?', raw_document, re.DOTALL)
            doc_title = re.match(r'.+?<TITLE>(?P<title>.+?)</TITLE>.+?', raw_document, re.DOTALL)
            doc_body = re.match(r'.+?<BODY>(?P<body>.+?)</BODY>.+?', raw_document, re.DOTALL)
           
            # If raw corpus has ID, it's a document, add it to list
            if doc_id:
                doc_id = int(doc_id.group('id'))
                # If it's not have title or body, put empty string instead of them 
                doc_title = doc_title.group('title') if doc_title else ''
                doc_body = doc_body.group('body') if doc_body else ''
                documents.append({'id': doc_id, 'title': doc_title, 'body':doc_body})
        return documents

    @classmethod
    def create_index(self, directory=None):
        """
            Creates index from data that in given directory
        """
        # Read files and get documents
        documents = self.read_files(directory)
        # Initialize directory and inverted index
        dictionary = {}
        inverted_index = {}
        # Load stop words from file
        stop_words = Tokenizer.stop_words()

        for document in documents:
            doc_id = document['id']
            # Concatenate title and body, then tokenize this combination
            tokens = Tokenizer.tokenize(document['title'] + ' ' + document['body'])
            # Iterate all tokens and if it's not a stop word, add it to index with it's position
            for position, token in enumerate(tokens):
                if not token in stop_words:
                    # Get ID of positional indexes of the token
                    postings_id = dictionary.get(token, self.get_posting_id())
                    # Get positional indexes of token as dictionary
                    postings = inverted_index.get(postings_id, {})
                    # Get positions of the token in the document as list
                    positions = postings.get(doc_id, [])
                    # Add this position to positional index
                    positions.append(position)
                    # Put positions list of the this document back to token's document's list
                    postings[doc_id] = positions
                    # Put updated positional indexes of the token back to inverted index
                    inverted_index[postings_id] = postings
                    # Update ID of the token in dictionary
                    dictionary[token] = postings_id
        # Save created index to file
        self.save_index(dictionary, inverted_index)
    
    @classmethod
    def get_posting_id(self):
        """
            Returns globally incremented ID for next postings list
        """
        self.POSTING_ID += 1
        return self.POSTING_ID - 1

    @classmethod
    def get_postings(self, token, dictionary, index):
        """
            Returns documents and positions of given token after normalization
        """
        stem = Tokenizer.normalize_and_stem(token)
        posting_id = dictionary.get(stem)
        return index.get(posting_id, {})

    @classmethod
    def save_index(self, directory, index):
        """
            Save dictionary and inverted index to file
        """
        pickle.dump(directory, open(self.DICTIONARY_NAME, 'wb'))
        pickle.dump(index, open(self.INDEX_NAME, 'wb'))

    @classmethod
    def get_index(self):
        """
            Load dictionary and inverted index from file
            Returns: 
                dictionary, index 
        """
        return pickle.load(open(self.DICTIONARY_NAME, 'rb')), pickle.load(open(self.INDEX_NAME, 'rb'))

    @classmethod
    def remove_index(self):
        """
            Removes old inverted index files
        """
        try:
            os.remove(self.DICTIONARY_NAME)
            os.remove(self.INDEX_NAME)
        except OSError:
            pass

    @classmethod
    def is_indexed(self):
        """
            Checks if index is exist
        """
        return os.path.isfile(self.DICTIONARY_NAME) and os.path.isfile(self.INDEX_NAME)