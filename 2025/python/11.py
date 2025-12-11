from collections import defaultdict
from functools import cache

puzzle_input = open('../inputs/11.txt').read()

nodes = None
reverse = None

def process_input():

    global nodes
    nodes = {
        ln[:ln.index(':')]: ln[ln.index(':') + 2:].split()
        for ln in puzzle_input.splitlines(keepends=False)
    }

    global reverse
    reverse = defaultdict(list)
    for src, dsts in nodes.items():
        for dst in dsts:
            reverse[dst].append(src)

def count_paths(src: str):

    @cache
    def count_paths_inner(dst: str, visits: frozenset[str] = frozenset()):
        if dst in visits:
            visits = visits.difference([dst])
        if dst == src:
            return 0 if visits else 1
        else:
            return sum(count_paths_inner(s, visits) for s in reverse[dst])

    return count_paths_inner

def solve_part_1():
    return count_paths('you')('out')

def solve_part_2():
    return count_paths('svr')('out', frozenset({ 'dac', 'fft' }))

process_input()
print(solve_part_1())
print(solve_part_2())