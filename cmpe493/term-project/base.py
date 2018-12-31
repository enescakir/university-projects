import re
import spell
import config
from string import digits, punctuation
from lemmatizer import Lemmatizer


class Tweet:
    """
        Encapsulates tweet and it's properties such as hashtags, mentions etc...
    """

    def __init__(self, words, hashtags, mentions, emojis):
        self.words = list(filter(lambda x: len(x) > 1, words))
        self.hashtags = hashtags
        self.mentions = mentions
        self.emojis = emojis


class Tokenizer:
    """
        Handles tokenize operations
    """

    @staticmethod
    def process(filename):
        global lemmatizer
        lemmatizer = Lemmatizer()

        raw_tweets = Tokenizer.parse(filename)

        raw_tweets, hashtags = Tokenizer.extract_hashtags(raw_tweets)

        raw_tweets, mentions = Tokenizer.extract_mentions(raw_tweets)

        raw_tweets, emojis = Tokenizer.extract_emojis(raw_tweets)

        tweets = []

        for text, hashtag, mention, emoji in zip(raw_tweets, hashtags, mentions, emojis):
            tweets.append(Tweet(Tokenizer.tokenize(
                text), hashtag, mention, emoji))

        return tweets

    @staticmethod
    def parse(filename):
        """
            Parse raw tweets from text files
        """

        # return [tweet.split('\t')[3] for tweet in open(filename).read().split('\n')]
        return open(filename).readlines()

    @staticmethod
    def tokenize(text):
        """
            Tokenizes given text after normalizing
            Normalizing steps:
                1. Make lowercase
                2. Remove links
                3. Remove stop words
                4. Remove punctuation
                5. Lemmatize
                6. Make localization
                7. Remove non alpha numeric characters
                8. Remove digits
        """

        text = Tokenizer.make_lower_case(text)
        text = Tokenizer.remove_links(text)
        # text = Tokenizer.remove_stop_words(text)
        # text = Tokenizer.remove_punctuation(text)

        tokens = text.split()
        if config.SPELL_CORRECTION:
            tokens = [spell.correction(token) for token in tokens]
        tokens = [lemmatizer.lemmatize(token) for token in tokens]
        # tokens = [Tokenizer.make_localization(token) for token in tokens]
        # tokens = [Tokenizer.remove_nonalphanumeric(token) for token in tokens]
        # tokens = [Tokenizer.remove_digits(token) for token in tokens]

        return tokens

    @staticmethod
    def make_lower_case(text):
        """
            Makes given text lower case
        """
        text = text.replace("İ", "i")
        return text.lower()

    @staticmethod
    def make_localization(text):
        """
            Makes given text locale
        """
        return text.translate(str.maketrans("ğçşıüö", "gcsiuo"))

    @staticmethod
    def remove_stop_words(text):
        """
            Removes stop words from given text
        """
        # Get stop word list from file
        stop_words = Tokenizer.stop_words()
        # Replace stop words with empty string
        remove_list_regex = re.compile(
            r'\b|\b'.join(map(re.escape, stop_words)))
        return remove_list_regex.sub('', text)

    @staticmethod
    def stop_words():
        """
            Returns stop word list from text file
        """
        return open('Utils/stopwords.txt').read().split()

    @staticmethod
    def remove_extra_whitespaces(text):
        """
            Removes extra whitespaces from given text such as multiple adjencent space
        """
        return re.sub(r'\s+', ' ', text).strip()

    @staticmethod
    def remove_punctuation(text):
        """
            Removes punctuations from given text
        """
        # Replace punctuation with space instead of remove it for
        # hand-to-mouth, six-week-old, euro-certificate
        text = re.sub(r'\'[a-z]{1,4}', " ", text)  # Removes 'nde 'de etc.
        return text.translate(str.maketrans(punctuation, ' ' * len(punctuation)))

    @staticmethod
    def remove_links(text):
        """
            Removes pic.twitter.com links from given text
        """
        return re.sub(r'pic.twitter.com/(\S+)', " ", text)

    @staticmethod
    def remove_digits(text):
        """
            Removes digits from given text
        """
        return text.translate(str.maketrans('', '', digits))

    @staticmethod
    def remove_nonalphanumeric(text):
        """
            Removes nonalphanumeric charachters from given text
        """
        return re.sub(r'[^a-zA-Z\d\s]', " ", text)

    @staticmethod
    def extract_hashtags(raw_tweets):
        """
            Extracts hashtags from given tweets
        """

        hashtags = [re.findall(r'#(\w+)', tweet) for tweet in raw_tweets]
        raw_tweets = [re.sub(r'#\w+', "", tweet) for tweet in raw_tweets]

        return raw_tweets, hashtags

    @staticmethod
    def extract_mentions(raw_tweets):
        """
            Extracts mentions from given tweets
        """

        mentions = [re.findall(r'@(\w+)', tweet) for tweet in raw_tweets]
        raw_tweets = [re.sub(r'@\w+', "", tweet) for tweet in raw_tweets]

        return raw_tweets, mentions

    @staticmethod
    def extract_emojis(raw_tweets):
        """
            Extracts emojis from given tweets
        """

        basic_emojis = [re.findall(r':[\)\/\(]+', tweet) for tweet in raw_tweets]
        raw_tweets = [re.sub(r':[\)\/\(]+', "", tweet) for tweet in raw_tweets]
        
        UNICODE_EMOJI_PATTERN = re.compile('[\U00010000-\U0010ffff]', flags=re.UNICODE)

        unicode_emojis = [UNICODE_EMOJI_PATTERN.findall(tweet) for tweet in raw_tweets]
        raw_tweets = [UNICODE_EMOJI_PATTERN.sub("", tweet) for tweet in raw_tweets]
        
        return raw_tweets, [basic_e + unicode_e for basic_e, unicode_e in zip(basic_emojis, unicode_emojis)]