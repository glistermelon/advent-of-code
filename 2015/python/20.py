from utils import prod
from sympy import isprime
from itertools import combinations
import functools
import math

@functools.cache
def is_prime(n : int):
    return isprime(n)

@functools.cache
def next_prime(n : int):
    n += 1
    while not is_prime(n):
        n += 1
    return n

def get_factors(ns : list[int]):
    yield 1
    done = set()
    for r in range(1, len(ns) + 1):
        for c in combinations(ns, r):
            p = prod(c)
            if p not in done:
                done.add(p)
                yield p

for part1 in True, False:

    target_presents = int(open('../inputs/20.txt').read())
    target_presents = math.ceil(target_presents / (10 if part1 else 11))
    lowest_h = target_presents

    def solve(prime_factors : list[int], products : list[int], sums : list[int]):

        global target_presents
        global lowest_h

        f = prime_factors[-1] if prime_factors else 2

        while True:

            p = f
            if products: p *= products[-1]
            if p >= lowest_h: break
            products.append(p)

            s = None
            if part1:
                if prime_factors and f == prime_factors[-1]:
                    prime_factors.append(f)
                    i = prime_factors.index(f) - 1
                    s = f**(prime_factors.count(f))
                    if i >= 0: s *= sums[i]
                    s += sums[-1]
                else:
                    prime_factors.append(f)
                    s = f + 1
                    if sums:
                        s *= sums[-1]
            else:
                prime_factors.append(f)
                s = sum(f for f in get_factors(prime_factors) if p // f <= 50)
            sums.append(s)

            end_recursion = s >= target_presents
            if end_recursion:
                lowest_h = p
            else:
                solve(prime_factors, products, sums)

            prime_factors.pop()
            products.pop()
            sums.pop()
            
            if end_recursion:
                break

            f = next_prime(f)

    solve([], [], [])

    print(lowest_h)