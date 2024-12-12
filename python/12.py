from util import *
from enum import Enum

board = open('../inputs/12.txt').read().splitlines(keepends=False)

def in_bounds(p):
    return p[0] >= 0 and p[1] >= 0 and p[0] < len(board) and p[1] < len(board[0])

def at(p):
    return board[p[0]][p[1]] if in_bounds(p) else None

class Dir(Enum):

    up = 0
    right = 1
    down = 2
    left = 3

    @staticmethod
    def from_num(n):
        return [Dir.up, Dir.right, Dir.down, Dir.left][n]

    def turn_right(self, n : int = 1):
        return Dir.from_num((self.value + n) % 4)

    def turn_left(self, n : int = 1):
        return Dir.from_num((self.value - n) % 4)
    
    def flip(self):
        return Dir.turn_right(self, 2)

    def advance(self, p, n = 1):
        if self == Dir.up:
            return (p[0] - n, p[1])
        if self == Dir.right:
            return (p[0], p[1] + n)
        if self == Dir.down:
            return (p[0] + n, p[1])
        if self == Dir.left:
            return (p[0], p[1] - n)
    
    @staticmethod
    def from_delta_pos(dy, dx):
        if dy > 0: return Dir.down
        if dy < 0: return Dir.up
        if dx > 0: return Dir.right
        if dx < 0: return Dir.left
        return None

def adj_points(p, include_dir = False):
    for dx in -1, 0, 1:
        for dy in -1, 0, 1:
            if dx != 0 and dy != 0: continue
            if include_dir:
                yield (p[0] + dy, p[1] + dx), Dir.from_delta_pos(dy, dx)
            else:
                yield (p[0] + dy, p[1] + dx)

def corner_points(p, include_dir = False):
    for dx in -1, 1:
        for dy in -1, 1:
            if include_dir:
                yield (p[0] + dy, p[1] + dx), Dir.from_delta_pos(dy, 0), Dir.from_delta_pos(0, dx)
            else:
                yield (p[0] + dy, p[1] + dx)

def expand_region(p, key = None, region = None):
    if region is None:
        region = set()
    if p in region: return
    first_call = len(region) == 0
    if first_call and key is None:
        key = lambda x : at(x) == at(p)
    elif not key(p): return
    region.add(p)
    for a in adj_points(p):
        expand_region(a, key, region)
    if first_call:
        return region

def perimeter(region):
    return sum(1 for p in region for a in adj_points(p) if a not in region)

def sides(region):
    out = 0
    for p in region:
        for a, dy, dx in corner_points(p, True):
            if (a not in region and dy.advance(p) in region and dx.advance(p) in region) or (dy.advance(p) not in region and not dx.advance(p) in region):
                out += 1
    return out

points = [(y, x) for y in range(len(board)) for x in range(len(board[0]))]
out = [0, 0]
while len(points) != 0:
    region = expand_region(points[0])
    for p in region:
        points.remove(p)
    out[0] += len(region) * perimeter(region)
    out[1] += len(region) * sides(region)
print(out[0])
print(out[1])