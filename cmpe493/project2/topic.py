from collections import Counter
import math
import operator

class Topic:  
      
    alpha = 1

    def __init__(self, name, documents, total_n_docs, vocabulary_length):
        self.name = name
        self.documents = documents
        self.text = []
        for doc in self.documents:
            self.text += doc.words
        self.total_n_docs = total_n_docs
        self.vocabulary_length = vocabulary_length

    def train_all_features(self):
        """
            Trains Naive bayes with all lexicon
        """
        print("Traning \"" + self.name + "\" class with all features")
        # Calculate P(c_j)
        self.prior = math.log2(len(self.documents) / self.total_n_docs)

        self.words_prob_all = {}
        self.words_doc_count = {}
        text_length = len(self.text)
        counter = Counter(self.text)

        for word in counter.keys():
            occurence = counter.get(word, 0)
            # Calculate P(w | c_j) for each word
            self.words_prob_all[word] = math.log2((occurence + self.alpha) / (text_length + self.alpha * self.vocabulary_length))
            
            # Calculate document occurence count for each word
            self.words_doc_count[word] = len([doc for doc in self.documents if word in doc.words])
    
    def get_word_prob_all(self, word):
        """
            Get conditional probability for word. If dictionary doesn't it, return smoothed value
        """
        return self.words_prob_all.get(word, math.log2(self.alpha / (len(self.text) + self.alpha * self.vocabulary_length)))

    def train_mutual(self, feature_vocabulary):
        """
            Trains Naive bayes with selected features by mutual information
        """
        print("Traning \"" + self.name + "\" class with mutual information")
        self.feature_vocabulary_length = len(feature_vocabulary)
        self.words_prob_mutual = {}
        self.feature_text = [word for word in self.text if word in feature_vocabulary]
        text_length = len(self.feature_text)
        counter = Counter(self.feature_text)
        for word in counter.keys():
            occurence = counter.get(word, 0)
            # Calculate P(w | c_j) for each word based on selected words
            self.words_prob_mutual[word] = math.log2((occurence + self.alpha) / (text_length + self.alpha * self.feature_vocabulary_length))

    def get_word_prob_mutual(self, word):
        """
            Get conditional probability that calculated with selected features for word. If dictionary doesn't it, return smoothed value
        """
        return self.words_prob_mutual.get(word, math.log2(self.alpha / (len(self.feature_text) + self.alpha * self.feature_vocabulary_length)))
    
    def get_words_doc_count(self, word, contain = True):
        """
            Get documents occurences count if contain is False it returns documents count doesn't contain
        """
        if contain:
            return self.words_doc_count.get(word, 0)
        else:
            return len(self.documents) - self.words_doc_count.get(word, 0)
    
    def select_features(self, topics, count):
        """
            Selects features via mutual information
        """
        print("Selecting feature for " + self.name + " class")
        # Calculate utilization for each word
        words_utility = {}
        for word in set(self.text):
            n11 = self.get_words_doc_count(word)
            n01 = self.get_words_doc_count(word, False)
            n10 = 0
            n00 = 0
            for topic in topics:
                if self.name != topic.name:
                    n10 += topic.get_words_doc_count(word)
                    n00 += topic.get_words_doc_count(word, False)
            n = n11 + n01 + n10 + n00
            words_utility[word] = ((n11 / n) * math.log2((n * n11 + 1) / ((n11 + n10) * (n11 + n01)))) + ((n01 / n) * math.log2((n * n01 + 1) / ((n01 + n00) * (n01 + n11)))) + ((n10 / n) * math.log2((n * n10 + 1) / ((n10 + n11) * (n10 + n00)))) + ((n00 / n) * math.log2((n * n00 + 1) / ((n00 + n01) * (n00 + n10))))
        # Get first 50 feature
        self.features = [x[0] for x in sorted(words_utility.items(), key=operator.itemgetter(1), reverse=True)[:50]]

