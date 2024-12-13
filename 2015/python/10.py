from itertools import groupby

seq = [int(c) for c in open('../inputs/10.txt').read()]

for i in range(50):
    if i == 40:
        print(len(seq))
    seq = [item for n, c in groupby(seq) for item in (len(list(c)), n)]
print(len(seq))