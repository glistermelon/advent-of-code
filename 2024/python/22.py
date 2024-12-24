import functools
import itertools
import collections
from utils import *

@functools.cache
def get_secret(n):
    n = ((n << 6) ^ n) % 16777216
    n = ((n >> 5) ^ n) % 16777216
    n = ((n << 11) ^ n) % 16777216
    return n

def get_secrets(n):
    for _ in range(2000):
        n = get_secret(n)
        yield n

def get_secrets_mod10(n):
    for _ in range(2000):
        n = get_secret(n)
        yield n % 10

@functools.cache
def get_nth_secret(s, n):
    return next(itertools.islice(get_secrets(s), n - 1, n))

start_secrets = list(map(int, open('../inputs/22.txt').read().splitlines(keepends=False)))

print(
    sum(get_nth_secret(n, 2000) for n in start_secrets)
)

# The runtime on this is like 30 seconds but I don't care enough to optimize it

banana_map = collections.defaultdict(int)
for s in start_secrets:
    delta_history = []
    for w in windows(get_secrets_mod10(s), 5):
        deltas = tuple(dw[1] - dw[0] for dw in windows(w, 2))
        if deltas not in delta_history:
            banana_map[deltas] += w[-1]
        delta_history.append(deltas)
print(max(banana_map.values()))