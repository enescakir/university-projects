import os
import re
from indexer import Indexer
from tokenizer import Tokenizer
import operator

documents = Indexer.read_files("original_data")

countA = 0
countB = 0

setC = set()
setD = set()

counterE = {}
counterF = {}

for document in documents:
    countA += len(document['title'].split())
    countA += len(document['body'].split())
    
    countB += len(Tokenizer.remove_stop_words(document['title']).split())
    countB += len(Tokenizer.remove_stop_words(document['body']).split())

    setC |= set(document['title'].split())
    setC |= set(document['body'].split())

    setD |= set([
        Tokenizer.stem(token) for token in Tokenizer.remove_stop_words( Tokenizer.make_lower_case(document['title']) ).split()
    ])

    setD |= set([
        Tokenizer.stem(token) for token in Tokenizer.remove_stop_words(Tokenizer.make_lower_case(document['body'])).split()
    ])

    for term in document['title'].split():
        count = counterE.get(term, 0)
        count += 1
        counterE[term] = count

    for term in document['body'].split():
        count = counterE.get(term, 0)
        count += 1
        counterE[term] = count

    for term in [Tokenizer.stem(token) for token in Tokenizer.remove_stop_words(Tokenizer.make_lower_case(document['title'])).split()]:
        count = counterF.get(term, 0)
        count += 1
        counterF[term] = count

    for term in [Tokenizer.stem(token) for token in Tokenizer.remove_stop_words(Tokenizer.make_lower_case(document['body'])).split()]:
        count = counterF.get(term, 0)
        count += 1
        counterF[term] = count

'''
 PRINT RESULTS
'''
print("\n(a) How many tokens does the corpus contain before stopword removal and stemming?")
print(countA)

print("\n(b) How many tokens does the corpus contain after stopword removal and stemming?")
print(countB)

print("\n(c) How many terms(unique tokens) are there before stopword removal, stemming, and case - folding?")
print(len(setC))

print("\n(d) How many terms(unique tokens) are there after stopword removal, stemming, and casefolding?")
print(len(setD))

print("\n(e) List the top 20 most frequent terms before stopword removal, stemming, and casefolding?")
tops = list(sorted(counterE.items(), key=operator.itemgetter(1), reverse=True))
for i in range(0, 20):
    print(tops[i])

print("\n(f) List the top 20 most frequent terms after stopword removal, stemming, and case - folding?")
tops = list(sorted(counterF.items(), key=operator.itemgetter(1), reverse=True))
for i in range(0, 20):
    print(tops[i])
