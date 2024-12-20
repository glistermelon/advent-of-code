from board_util import *
from util import *

board = Board(open('../inputs/20.txt').read().splitlines(keepends=False))

scores = {}
pos = find_nd(board.arr, 'E')[0]
scores[pos] = 0
end_pos = find_nd(board.arr, 'S')[0]

@functools.cache
def tile_open(at_p):
    return at_p is not None and at_p in '.SE'

for cheat_radius in 2, 20:

    queue = [pos]
    visited = set()
    visited.add(pos)
    while queue:
        pos = queue.pop(0)
        s = scores[pos] + 1
        for p in adj_points(pos):
            if p not in visited and tile_open(board[p]):
                queue.append(p)
                scores[p] = s
                visited.add(p)

    output = 0
    for y in range(board.h):
        for x in range(board.w):
            p = (y, x)
            if not tile_open(board[p]):
                continue
            for a, d in adj_points(p, radius=cheat_radius, inclusive=True, include_dir = True):
                if not tile_open(board[a]):
                    continue
                dist = dist_taxi(p, a)
                if dist < 2:
                    continue
                score_delta = scores[p] - scores[a]
                if score_delta == dist:
                    continue
                if score_delta - dist >= 100:
                    output += 1

    print(output)