from dataclasses import dataclass
from itertools import combinations
from math import sqrt

puzzle_input = open('../inputs/8.txt').read()

@dataclass(frozen=True)
class Junction:
    x: int
    y: int
    z: int

def dist(j1: Junction, j2: Junction):
    return sqrt((j1.x - j2.x)**2 + (j1.y - j2.y)**2 + (j1.z - j2.z)**2)

def prod(i):
    i = iter(i)
    p = next(i)
    while (x := next(i, None)) is not None:
        p *= x
    return p

junctions = None

def steps():
    shortest = sorted(combinations(junctions, 2), key=lambda c:dist(*c))
    circuits = { j: { j } for j in junctions }
    for j1, j2 in shortest:
        circuits[j1].update(circuits[j2])
        for j in circuits[j2]:
            circuits[j] = circuits[j1]
        yield circuits, (j1, j2)

def process_input():
    global junctions
    junctions = [Junction(*map(int, ln.split(','))) for ln in puzzle_input.splitlines(keepends=False)]

def solve_part_1():
    gen = steps()
    circuits = None
    for _ in range(1000):
        circuits, _ = next(gen)
    circuits = list(circuits.values())
    circuits = [c for i, c in enumerate(circuits) if not any(p is c for p in circuits[:i])]
    return prod(len(c) for c in sorted(circuits, key=len, reverse=True)[:3])

def solve_part_2():
    for circuits, (j1, j2) in steps():
        if (len(circuits[j1]) == len(junctions)):
            return j1.x * j2.x

process_input()
print(solve_part_1())
print(solve_part_2())