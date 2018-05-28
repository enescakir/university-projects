
from reader import Reader
from topic import Topic


# TRANING
# traning_docs, test_docs = Reader.read_files('test')
traning_docs, test_docs = Reader.read_files('reuters21578')

number_of_docs = len(traning_docs)

# Find vocabulary length
vocabulary = []
for doc in traning_docs:
    vocabulary += doc.words
vocabulary = set(vocabulary)
vocabulary_length = len(vocabulary)

topics = ["earn", "acq", "money-fx", "grain", "crude"]

knowledge = {}

# Train topic with all lexicons
for topic in topics:
    t = Topic(name=topic, documents=[doc for doc in traning_docs if doc.topic == topic], total_n_docs=number_of_docs, vocabulary_length=vocabulary_length)
    t.train_all_features()
    knowledge[topic] = t

# Select features
feature_vocabulary = []
for topic in knowledge.values():
    topic.select_features(knowledge.values(), 50)
    feature_vocabulary += topic.features

# Find feature vocabulary length
feature_vocabulary = set(feature_vocabulary)
feature_vocabulary_length = len(feature_vocabulary)

for topic in knowledge.values():
    topic.train_mutual(feature_vocabulary)

# TESTING
print("Testing documents")
for doc in test_docs:
    doc.apply_bayes_with_all_features(knowledge.values(), vocabulary)
    doc.apply_bayes_with_mutual(knowledge.values(), feature_vocabulary)

# Calculating performance
measures = {}
measures['contingency'] = { "all": { "tp":0, "fp":0, "fn":0, "tn":0 }, "mutual": { "tp":0, "fp":0, "fn":0, "tn":0 } }
measures['macro_total'] = { "all": { "precision":0, "recall":0, "f":0 }, "mutual": { "precision":0, "recall":0, "f":0 } }

for topic in topics:
    measures[topic] = { "all": { "tp":0, "fp":0, "fn":0, "tn":0 }, "mutual": { "tp":0, "fp":0, "fn":0, "tn":0 } }
    for doc in test_docs:
        if topic == doc.topic:
            # Truth YES

            # All Lexicon
            if doc.topic == doc.guess_all:
                # Classifier YES
                measures[topic]["all"]["tp"] += 1
                measures['contingency']["all"]["tp"] += 1
            else:
                # Classifier NO
                measures[topic]["all"]["fn"] += 1
                measures['contingency']["all"]["fn"] += 1

            # Mutual Information
            if doc.topic == doc.guess_mutual:
                # Classifier YES
                measures[topic]["mutual"]["tp"] += 1
                measures['contingency']["mutual"]["tp"] += 1
            else:
                # Classifier NO
                # Classifier NO
                measures[topic]["mutual"]["fn"] += 1
                measures['contingency']["mutual"]["fn"] += 1
        else:
            # Truth NO
            # All Lexicon
            if doc.topic == doc.guess_all:
                # Classifier YES
                measures[topic]["all"]["tn"] += 1
                measures['contingency']["all"]["tn"] += 1
            else:
                # Classifier NO
                measures[topic]["all"]["fp"] += 1
                measures['contingency']["all"]["fp"] += 1

            # Mutual Information
            if doc.topic == doc.guess_mutual:
                # Classifier YES
                measures[topic]["mutual"]["tn"] += 1
                measures['contingency']["mutual"]["tn"] += 1
            else:
                # Classifier NO
                measures[topic]["mutual"]["fp"] += 1
                measures['contingency']["mutual"]["fp"] += 1

print("\n\t\t\t\t\tDOCUMENT COUNTS")
print("Train")
total = 0
for key, topic in knowledge.items():
    print(topic.name + ": " + str(len(topic.documents)))
    total += len(topic.documents)
print("Total: " + str(total))

print("\n\nTest")
total = {}
for doc in test_docs:
    count = total.get(doc.topic, 0)
    count += 1
    total[doc.topic] = count
for topic, count in total.items():
    print(topic + ": " + str(count))
print("Total: " + str(sum(total.values())))

print("\n\t\t\t\t\tSELECTED FEATURES")
for key, topic in knowledge.items():
    print(topic.name)
    print(topic.features)
    print("")

print("\n\t\t\t\t\tPERFORMANCE VALUES")
# Calculate precision, recall and f-measure
for topic, measure in measures.items():
    if topic != "macro_total":
        for classiﬁer, values in measure.items():
            precision = measures[topic][classiﬁer]['tp'] / (measures[topic][classiﬁer]['tp'] + measures[topic][classiﬁer]['fp'])
            recall = measures[topic][classiﬁer]['tp'] / (measures[topic][classiﬁer]['tp'] + measures[topic][classiﬁer]['fn'])
            f = (2 * precision * recall) / (precision + recall)
            
            measures[topic][classiﬁer]['precision'] = precision
            measures[topic][classiﬁer]['recall'] = recall
            measures[topic][classiﬁer]['f'] = f

            if topic != 'contingency':
                print("\n" + topic + " - " + classiﬁer)
                print("\tPrecision: " + str(precision))
                print("\tRecall:    " + str(recall))
                print("\tF-measure: " + str(f))
                measures['macro_total'][classiﬁer]['precision'] += precision
                measures['macro_total'][classiﬁer]['recall'] += recall
                measures['macro_total'][classiﬁer]['f'] += f

print("\nTraning with All Lexicon")
print("\tMacro-Averaged Precision: " + str(measures['macro_total']['all']['precision'] / len(topics)))
print("\tMicro-Averaged Precision: " + str(measures['contingency']['all']['precision']))
print("\n\tMacro-Averaged Recall:    " + str(measures['macro_total']['all']['recall'] / len(topics)))
print("\tMicro-Averaged Recall:    " + str(measures['contingency']['all']['recall']))
print("\n\tMacro-Averaged F-measure: " + str(measures['macro_total']['all']['f'] / len(topics)))
print("\tMicro-Averaged F-measure: " + str(measures['contingency']['all']['f']))

print("\n\nTraning with Selected Features by Mutual Information")
print("\tMacro-Averaged Precision: " + str(measures['macro_total']['mutual']['precision'] / len(topics)))
print("\tMicro-Averaged Precision: " + str(measures['contingency']['mutual']['precision']))
print("\n\tMacro-Averaged Recall:    " + str(measures['macro_total']['mutual']['recall'] / len(topics)))
print("\tMicro-Averaged Recall:    " + str(measures['contingency']['mutual']['recall']))
print("\n\tMacro-Averaged F-measure: " + str(measures['macro_total']['mutual']['f'] / len(topics)))
print("\tMicro-Averaged F-measure: " + str(measures['contingency']['mutual']['f']))