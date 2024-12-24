from utils import *
from board_util import *

falls = [tuple(map(int, ln.split(','))) for ln in open('../inputs/18.txt').read().splitlines(keepends=False)]

def get_board():
    return Board([['.' for x in range(71)] for y in range(71)])

# Part 1

board = get_board()

for fall in falls[:1024]:
    board[fall] = '#'
queue = [(0, 0)]
visited = set()
visited.add((0, 0))
step = 0
while queue:
    next_queue = []
    do_break = False
    for p in queue:
        if p == (70, 70):
            print(step)
            do_break = True
            break
        new_points = [s for s in adj_points(p) if board[s] == '.' and s not in visited]
        next_queue += new_points
        for np in new_points:
            visited.add(np)
    if do_break:
        break
    queue = next_queue
    step += 1

# Part 2

board = get_board()

left = set()
right = set()
bottom = set()
top = set()

for fall in falls:
    board[fall] = '#'
    if fall[0] == 0:
        top.add(fall)
    elif fall[0] == 70:
        bottom.add(fall)
    if fall[1] == 0:
        left.add(fall)
    elif fall[1] == 70:
        right.add(fall)
    marks = []
    for p in surrounding_points(fall):
        if p in left: marks.append(left)
        if p in right: marks.append(right)
        if p in bottom: marks.append(bottom)
        if p in top: marks.append(top)
    queue = [fall]
    visited = set()
    while queue:
        p = queue.pop(0)
        visited.add(p)
        do_continue = True
        for m in marks:
            if p not in m: do_continue = False
            m.add(p)
        if do_continue:
            continue
        if (p in bottom and p in top) or (p in left and p in right) or (p in left and p in top) or (p in bottom and p in right):
            print(','.join(map(str, fall)))
            exit()
        queue += list(s for s in surrounding_points(p) if board[s] == '#' and s not in visited)