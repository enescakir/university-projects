import operator

class Document:
    """
        Handles documents
    """
    
    def __init__(self, id, words, topic):
        self.id = id
        self.words = words
        self.topic = topic

    def apply_bayes_with_all_features(self, topics, vocabulary):
        """
            Guesses topic by naive bayes that trained by all lexicon
        """
        scores = {}
        for topic in topics:
            score = topic.prior
            for word in self.words:
                if word in vocabulary:
                    score += topic.get_word_prob_all(word)
            scores[topic.name] = score
        self.guess_all = max(scores.items(), key=operator.itemgetter(1))[0]
    
    def apply_bayes_with_mutual(self, topics, vocabulary):
        """
            Guesses topic by naive bayes that trained by selected features
        """
        scores = {}
        for topic in topics:
            score = topic.prior
            for word in self.words:
                if word in vocabulary:
                    score += topic.get_word_prob_mutual(word)
            scores[topic.name] = score
        self.guess_mutual = max(scores.items(), key=operator.itemgetter(1))[0]