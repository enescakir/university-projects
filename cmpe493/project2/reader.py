import os, re, pickle
from tokenizer import Tokenizer
from document import Document

class Reader:
    """
        Handles reading operations
    """
    
    topics = ["earn", "acq", "money-fx", "grain", "crude"]

    @classmethod
    def read_files(self, directory=None):
        """
            Returns read documents from data directory
        """
        print("Reading files")
        # If no directory is given, set it to current directory
        directory = os.getcwd() if directory is None else directory
        filenames = os.listdir(directory)
        # Get all file with .sgm extension
        filenames = [filename for filename in filenames if filename.endswith(".sgm")]
        filenames.sort()
        traning_docs = []
        test_docs = []
        # Extract documents from each file
        print("Extracting documents")
        for filename in filenames:
            raw_data = open(os.path.join(directory, filename), "r", encoding="latin-1").read()
            traning, test = self.extract_documents(raw_data)
            traning_docs += traning
            test_docs += test
        return traning_docs, test_docs

    @classmethod
    def extract_documents(self, raw_data):
        """
            Extracts documents from raw string
        """
        traning_docs = []
        test_docs = []
        # Seperate each document
        raw_documents = raw_data.split('</REUTERS>')
        # Extract information from each raw document string
        for raw_document in raw_documents:
            doc_id = re.match(r'.+?NEWID=\"(?P<id>\d+)\">.+?', raw_document, re.DOTALL)
            doc_title = re.match(r'.+?<TITLE>(?P<title>.+?)</TITLE>.+?', raw_document, re.DOTALL)
            doc_body = re.match(r'.+?<BODY>(?P<body>.+?)</BODY>.+?', raw_document, re.DOTALL)
            doc_topics = re.match(r'.+?<TOPICS>(?P<topics>.+?)</TOPICS>.+?', raw_document, re.DOTALL)
            if doc_topics:
                doc_topics = re.findall(r'.*?<D>(?P<topics>.+?)</D>.*?', doc_topics.group('topics'), re.DOTALL)
            doc_type = re.findall(r'LEWISSPLIT=\"(?P<type>\w+?)\"', raw_document)
            doc_type = doc_type[0] if len(doc_type) == 1 else None

            # If raw corpus has ID, it's a document, add it to list
            if doc_id and doc_topics and doc_type:
                intersect = list(set(self.topics) & set(doc_topics))
                if len(intersect) == 1:
                    doc_id = int(doc_id.group('id'))
                    # If it's not have title or body, put empty string instead of them 
                    doc_title = doc_title.group('title') if doc_title else ''
                    doc_body = doc_body.group('body') if doc_body else ''
                    doc_class = intersect[0]
                    doc = Document(id=doc_id, words=Tokenizer.tokenize(doc_title + " " + doc_body), topic=doc_class)
                    if doc_type == "TRAIN":
                        traning_docs.append(doc)
                    elif doc_type == "TEST":
                        test_docs.append(doc)
        return traning_docs, test_docs