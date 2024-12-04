import re
from operator import mul
from functools import reduce

# Part 1

data = open('inputs/3.txt').read()
output = 0
while True:
    s = re.search('mul\\([0-9]+,[0-9]+\\)', data)
    if s is None: break
    s = s.span()
    m = data[s[0] + 4:s[1] - 1].split(',')
    if len(m[0]) <= 3 and len(m[1]) <= 3:
        output += reduce(mul, (int(n) for n in m), 1)
    data = data[:s[0]] + data[s[1]:]
print(output)

# Part 2

data = open('inputs/3.txt').read()

while True:
    i1 = data.find('don\'t()')
    if i1 == -1: break
    i2 = data.find('do()', i1 + 7)
    data = data[:i1] + (data[i2 + 4:] if i2 != -1 else '')

output = 0
while True:
    s = re.search('mul\\([0-9]+,[0-9]+\\)', data)
    if s is None: break
    s = s.span()
    m = data[s[0] + 4:s[1] - 1].split(',')
    if len(m[0]) <= 3 and len(m[1]) <= 3:
        output += reduce(mul, (int(n) for n in m), 1)
    data = data[:s[0]] + data[s[1]:]
print(output)