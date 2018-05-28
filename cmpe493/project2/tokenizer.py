import re
from string import punctuation, digits

class Tokenizer:
    """
        Handles tokenize operations
    """


    @staticmethod
    def tokenize(text):
        """
            Tokenizes given text after normalizing
        """
        # Normalize given text
        text = Tokenizer.normalize(text)
        # Split text from whitespaces
        return text.split()

    @staticmethod
    def make_lower_case(text):
        """
            Makes given text lower case
        """
        return text.lower()

    @staticmethod
    def remove_stop_words(text):
        """
            Removes stop words from given text
        """
        # Get stop word list from file
        stop_words = Tokenizer.stop_words()
        # Replace stop words with empty string
        remove_list_regex = re.compile(r'\b|\b'.join(map(re.escape, stop_words)))
        return remove_list_regex.sub('', text)

    @staticmethod
    def stop_words():
        """
            Returns stop word list from text file
        """
        return open('stopwords.txt').read().split()

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
        # Replace punctuation with space instead of remove it for hand-to-mouth, six-week-old, euro-certificate
        return text.translate(str.maketrans(punctuation, ' ' * len(punctuation)))

    @staticmethod
    def remove_digits(text):
        """
            Removes digits from given text
        """
        return text.translate(str.maketrans('', '', digits))

    @staticmethod
    def normalize(text):
        """
            Normalizes given text
            Steps:
                1. Make lowercase
                2. Remove punctuation
                3. Remove digit
                4. Remove extra whitespace
        """
        text = Tokenizer.make_lower_case(text)
        text = Tokenizer.remove_stop_words(text)
        text = Tokenizer.remove_punctuation(text)
        return text