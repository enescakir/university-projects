import config
from estimator import Estimator

e = Estimator()
preds = e.predict('Testset/test.txt')

if not config.TEST:
    out = open('result.txt', 'w')
    for pred in preds:
        if pred == 'positive':
            print(1)
            out.write('1\n')
        elif pred == 'notr':
            print(0)
            out.write('0\n')
        else:
            print(-1)
            out.write('-1\n')
else:
    labels = open('Testset/label.txt').read().split('\n')

    c = 0
    for pred,real in zip(preds, labels):
        if(pred == real):
            c += 1
    print("%.5lf" %(c/len(preds)))