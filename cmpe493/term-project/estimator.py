import nltk
import config
import random
import sklearn
import constants
import numpy as np

from base import Tokenizer, Tweet
from numpy.random import permutation
from sklearn.naive_bayes import MultinomialNB
from sklearn.feature_selection import SelectKBest
from sklearn.neighbors import KNeighborsClassifier
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.feature_selection import chi2, mutual_info_regression, mutual_info_classif


class Estimator:
    def __init__(self):
        self.vocabulary = {}
        self.class_names = ['positive', 'negative', 'notr']
        self.terms = []
        self.training_tweets = self.get_tweets()
        self.create_vocabulary()
        self.train_naive_bayes()
        self.train_knn()
    
    def read_file(self, filename, class_name='Unknown'):
        return [{'tweet': x, 'class': class_name} for x in Tokenizer.process(filename)]  
    
    def get_tweets(self):
        tweets = []
        for class_name in self.class_names:
            folder_name = 'Testset' if config.TEST else 'Dataset'
            tweets += self.read_file(folder_name + '/' + class_name + '-train', class_name)
        tweets =  random.sample(tweets, len(tweets))
        return tweets

    def find_tweet_counts(self, tweets):
        tweet_counts = np.zeros((len(tweets), len(self.terms)))
        
        for i in range(len(tweets)):
            for word in tweets[i]['tweet'].words:
                if word in self.vocabulary:
                    tweet_counts[i][self.vocabulary[word]] += 1
            if config.HASHTAG:
                for hashtag in tweets[i]['tweet'].hashtags:
                    if '#' + hashtag in self.vocabulary:
                        tweet_counts[i][self.vocabulary['#' + hashtag]] += 1
            if config.EMOJI:
                for emoji in tweets[i]['tweet'].emojis:
                    if emoji in self.vocabulary:
                        tweet_counts[i][self.vocabulary[emoji]] += 1
        return tweet_counts

    def fillVocabulary(self, tweet):
        for word in tweet.words:
            self.vocabulary[word] = True
        if config.HASHTAG:
            for hashtag in tweet.hashtags:
                self.vocabulary['#' + hashtag] = True
        if config.EMOJI:
            for emoji in tweet.emojis:
                self.vocabulary[emoji] = True
        
    def create_vocabulary(self):
        for tweet in self.training_tweets:
            self.fillVocabulary(tweet['tweet'])

        for i, term in enumerate(self.vocabulary):
            self.terms.append(term)
            self.vocabulary[term] = i

    def train_naive_bayes(self):
        # training_tfidf = TfidfTransformer().fit_transform(self.find_tweet_counts(self.training_tweets)) #tf-idf vectors
        training_tfidf = self.find_tweet_counts(self.training_tweets)
        training_class = np.array([self.class_names.index(x['class']) for x in self.training_tweets]) #Labels (0-1-2) means (positive-negative-notr)

        clf = MultinomialNB().fit(training_tfidf, training_class)
        self.clf = clf
    
    def train_knn(self):
        training_tfidf = TfidfTransformer().fit_transform(self.find_tweet_counts(self.training_tweets)) #tf-idf vectors
        training_class = np.array([self.class_names.index(x['class']) for x in self.training_tweets]) #Labels (0-1-2) means (positive-negative-notr)

        neigh = KNeighborsClassifier(n_neighbors=constants.KNN_NEIGHBORS)
        neigh.fit(training_tfidf, training_class)
        self.neigh = neigh

    def predict(self, filename):
        neigh = self.neigh
        clf = self.clf

        test_tweets = self.read_file(filename)
        
        # test_tfidf = TfidfTransformer().fit_transform(self.find_tweet_counts(test_tweets))
        test_tfidf = self.find_tweet_counts(test_tweets)

        preds = []
        knn_preds = neigh.predict(test_tfidf)
        knn_probs = neigh.predict_proba(test_tfidf)
        nb_preds = clf.predict(test_tfidf)

        for knn_pred, knn_prob, nb_pred in zip(knn_preds, knn_probs, nb_preds):
            if knn_prob.max() > constants.KNN_THRESHOLD:
                preds.append(self.class_names[knn_pred])
            else:
                preds.append(self.class_names[nb_pred])

        return preds

if __name__ == '__main__':
    for i in range(100):
        n = Estimator()
        n.predict('test.txt')