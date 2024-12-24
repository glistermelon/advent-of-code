from utils import *

stones = [int(s) for s in open('../inputs/11.txt').read().split()]
stones = { s : stones.count(s) for s in stones }

def add_to_map(m, k, v):
    try:
        m[k] += v
    except:
        m[k] = v

def blink(stones):
    new = {}
    for s, c in stones.items():
        if s == 0:
            add_to_map(new, 1, c)
            continue
        ss = str(s)
        if len(ss) % 2 == 0:
            add_to_map(new, int(ss[len(ss) // 2:]), c)
            add_to_map(new, int(ss[:len(ss) // 2]), c)
        else:
            add_to_map(new, s * 2024, c)
    return new

for i in range(75):
    if i == 25:
        print(sum(stones.values()))
    stones = blink(stones)
print(sum(stones.values()))