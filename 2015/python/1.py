data = open('../inputs/1.txt').read()

print(data.count('(') - data.count(')'))

floor = 0
for i, c in enumerate(data):
    floor += 1 if c == '(' else -1
    if floor == -1:
        print(i + 1)
        break