from dataclasses import dataclass

puzzle_input = open('../inputs/12.txt').read()

@dataclass
class Problem:
    w: int
    h: int
    presents: list[int]

problems: list[Problem] = None

def process_input():
    global problems
    problems = []
    for ln in puzzle_input.splitlines(keepends=False):
        i = ln.index(':')
        w, h = map(int, ln[:i].split('x'))
        presents = map(int, ln[i + 2:].split())
        problems.append(Problem(w, h, presents))

def solve_part_1():
    return sum(1 for p in problems if sum(p.presents) <= (p.w // 3) * (p.h // 3))

process_input()
print(solve_part_1())