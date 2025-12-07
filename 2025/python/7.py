from functools import cache

puzzle_input = open('../inputs/7.txt').read()

map = None
start_beam = None

def process_input():

    global map
    map = [[c for c in row] for row in puzzle_input.splitlines(keepends=False)]

    global start_beam
    start_beam = map[0].index('S')

def solve_part_1():
    splits = 0
    beams = { start_beam }
    for row in map[1:]:
        new_beams = set()
        for beam in beams:
            if row[beam] == '^':
                new_beams.add(beam - 1)
                new_beams.add(beam + 1)
                splits += 1
            else:
                new_beams.add(beam)
        beams = new_beams
    return splits

@cache
def count_timelines(x, y):
    while map[y][x] != '^':
        y += 1
        if y == len(map):
            return 1
    return count_timelines(x - 1, y) + count_timelines(x + 1, y)

def solve_part_2():
    return count_timelines(start_beam, 0)

process_input()
print(solve_part_1())
print(solve_part_2())