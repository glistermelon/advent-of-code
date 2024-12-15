from util import *
from sympy import isprime
from math import comb
from itertools import combinations
import functools
from math import ceil


import time
start_time = time.time()

@functools.cache
def is_prime(n : int):
    return isprime(n)

def get_divisors(ns : list[int]):
    yield 1
    done = set()
    for r in range(1, len(ns) + 1):
        for c in combinations(ns, r):
            p = prod(c)
            if p not in done:
                done.add(p)
                yield p

def get_divisors2(ns : list[int], f):
    done = set()
    for r in range(1, len(ns) + 1):
        for c in combinations(ns, r):

            cp = list(ns)
            for n in c:
                cp.remove(n)
            if f in cp:
                continue

            p = prod(c)
            print(p, cp)
            if p not in done:
                done.add(p)
                yield p

target_presents = 36000000

lowest_prod = target_presents // 10

# def solve(targ : int, ns : list[int], prods : list[int], sums : list[int]):

#     global lowest_prod

#     sols = []
#     n = ns[-1] if len(ns) else 2
#     if n != 2:
#         print(ns)
#         exit()
#     while True:

#         p = n
#         if len(prods):
#             p *= prods[-1]
#         if p >= lowest_prod:
#             break
#         prods.append(p)

#         ns.append(n)
#         #s = sum(d for d in get_divisors(ns) if p // d <= 50)
#         s = sum(get_divisors(ns))
#         sums.append(s)

#         end_recursion = s >= targ
#         if end_recursion:
#             lowest_prod = p
#             sols.append(list(ns))
#         else:
#             sols += solve(targ, ns, prods, sums)
#         ns.pop()
#         prods.pop()
#         sums.pop()
#         if end_recursion:
#             break

#         n += 1
#         while not is_prime(n):
#             n += 1

#     return sols


A = [2,2,3,5]
F = list(get_divisors(A))
print(sum(F), len(F), F)

print(list(get_divisors2(A, 3)))

A.append(3)
F = list(
    get_divisors(A))
print(sum(F), len(F), F)

exit()


target_presents = 36000000
target_presents = ceil(target_presents / 10) # Clarification 1
lowest_h = target_presents # Clarification 2

def solve(prime_factors, products):

    global lowest_h
    global target_presents
    f = prime_factors[-1] if len(prime_factors) else 2 # Clarification 3

    while True:

        p = products[-1] * f if len(products) else f
        if p >= lowest_h: break # Clarification 4
        prime_factors.append(f)
        products.append(p)
        s = sum(get_divisors(prime_factors))
        if s >= target_presents:
            lowest_h = p # Clarification 4
            prime_factors.pop()
            products.pop()
            break
        else:
            solve(prime_factors, products)
            prime_factors.pop()
            products.pop()
        
        f += 1
        while not is_prime(f): # Clarification 5
            f += 1

solve([], [])
print(lowest_h)

# elves = []

# h = 1
# off = 0

# while True:

#     p = 0

#     elves.append(1)
#     if h / (off + 1) > 50:
#         elves.pop(0)
#         off += 1

#     for i, c in enumerate(elves):
#         c -= 1
#         if c == 0:
#             n = i + 1 + off
#             p += 11 * n
#             elves[i] = n
#         else:
#             elves[i] = c
    
#     if p >= 36000000:
#         print(h)
#         break

#     if h % 10000 == 0:
#         print(h)

#     h += 1


print(time.time() - start_time)