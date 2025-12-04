from itertools import product
from copy import deepcopy

puzzle_input = open('../inputs/4.txt').read()

map = None

def process_input():
    global map
    map = [[False] + [c == '@' for c in ln] + [False] for ln in puzzle_input.splitlines(keepends=False)]
    empty_row = [False] * len(map[0])
    map = [empty_row] + map + [empty_row]

def do_one_sweep(map):
    removed = []
    for y, row in enumerate(map):
        for x, v in enumerate(row):
            if not v:
                continue
            adj = sum(
                1 for dx, dy in product(range(-1, 2), range(-1, 2))
                if (not (dx == 0 and dy == 0)) and map[y + dy][x + dx]
            )
            if adj < 4:
                removed.append((y, x))
    for y, x in removed:
        map[y][x] = False
    return len(removed)

def solve_part_1():
    return do_one_sweep(deepcopy(map))

def solve_part_2():
    result = 0
    while sweep_result := do_one_sweep(map):
        result += sweep_result
    return result
        

process_input()
print(solve_part_1())
print(solve_part_2())