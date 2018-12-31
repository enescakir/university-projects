import random
tweets = []
files = {}
class_names = ['positive', 'negative', 'notr']
for class_name in class_names:
    data = open('Dataset/'+class_name+'-train').read().split('\n')

    for x in data:
        tweets.append({'tweet': x, 'class': class_name})

    files[class_name] = open('Testset/'+class_name+'-train', 'w')

tweets =  random.sample(tweets, len(tweets))

train_number = int(len(tweets) * 0.90)

first = True
for tweet in tweets[:train_number]:
    data = tweet['tweet']
    class_name = tweet['class']
    if not first:
        files[class_name].write("\n")
    else:
        first = False
    files[class_name].write("%s" % data)
    
test_file = open('Testset/test.txt', 'w')
label_file = open('Testset/label.txt', 'w')

first = True
for tweet in tweets[train_number:]:
    data = tweet['tweet']
    class_name = tweet['class']
    if not first:
        test_file.write("\n")
        label_file.write("\n")
    else:
        first = False
    test_file.write("%s" % data)
    label_file.write("%s" % class_name)