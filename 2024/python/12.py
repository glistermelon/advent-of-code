from util import *
from enum import Enum
from board_util import *

board = Board(open('../inputs/12.txt').read().splitlines(keepends=False))

points = [(y, x) for y in range(board.h) for x in range(board.w)]
out = [0, 0]
while len(points) != 0:
    region = board.expand_region(points[0])
    for p in region:
        points.remove(p)
    out[0] += len(region) * perimeter(region)
    out[1] += len(region) * sides(region)
print(out[0])
print(out[1])