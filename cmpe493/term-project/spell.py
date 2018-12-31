import re
from collections import Counter

DICTIONARY_FILE = 'Utils/zargan_freqs_small.txt'
# DIRECTORY OPTIONS
# zargan_freqs_small.txt      ->  Zargan Dictionary most common 100000 words
# zargan_freqs_big.txt        ->  Zargan Dictionary words that has 6+ occurences

def words(text): return re.findall(r'\w+', text.lower())

lines = [line.split() for line in open(DICTIONARY_FILE).readlines()]
WORDS = Counter({line[0]: int(line[1]) for line in lines})
# WORDS = Counter(words(open('Utils/big.txt').read()))

# Increase speed x 2, don't correct same word again and again
WORD_CACHE = {}

def P(word, N=sum(WORDS.values())): 
    "Probability of `word`."
    return WORDS[word] / N

def correction(word): 
    "Most probable spelling correction for word."
    cache = WORD_CACHE.get(word)
    if cache is None:
        correct = max(candidates(word), key=P)
        WORD_CACHE[word] = correct
        # if correct != word:
        #     print(word + " -> " + correct)
        return correct
    else:
        return cache

def candidates(word): 
    "Generate possible spelling corrections for word."
    return (known([word]) or known(edits1(word)) or known(edits2(word)) or [word])

def known(words): 
    "The subset of `words` that appear in the dictionary of WORDS."
    return set(w for w in words if w in WORDS)

def edits1(word):
    "All edits that are one edit away from `word`."
    letters    = 'abcdefghijklmnopqrstuvwxyzüıöşğç'
    splits     = [(word[:i], word[i:])    for i in range(len(word) + 1)]
    deletes    = [L + R[1:]               for L, R in splits if R]
    transposes = [L + R[1] + R[0] + R[2:] for L, R in splits if len(R)>1]
    replaces   = [L + c + R[1:]           for L, R in splits if R for c in letters]
    inserts    = [L + c + R               for L, R in splits for c in letters]
    return set(deletes + transposes + replaces + inserts)

def edits2(word): 
    "All edits that are two edits away from `word`."
    return (e2 for e1 in edits1(word) for e2 in edits1(e1))
