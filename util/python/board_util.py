from util import *
from enum import Enum

class Board:

    def __init__(self, arr_2d):
        self.arr = arr_2d
        self.w = len(arr_2d)
        self.h = len(arr_2d[0])
    
    def __getitem__(self, p):
        return self.at(p)
    
    def __setitem__(self, p, v):
        if self.in_bounds(p):
            self.arr[p[0]][p[1]] = v
    
    def __str__(self):
        lines = []
        for r in self.arr:
            try:
                check = True
                for item in r:
                    if type(item) is not str or len(item) != 1:
                        check = False
                        break
                if check:
                    r = ''.join(r)
            except:
                pass
            lines.append(str(r))
        bar = '-' * len(lines[0])
        return bar + '\n' + '\n'.join(lines) + '\n' + bar
    
    def copy_arr(self):
        return list(list(r) for r in self.arr)
    
    def copy(self):
        return Board(self.copy_arr())

    def in_bounds(self, p):
        return p[0] >= 0 and p[1] >= 0 and p[0] < len(self.arr) and p[1] < len(self.arr[0])

    def at(self, p):
        return self.arr[p[0]][p[1]] if self.in_bounds(p) else None

    def expand_region(self, p, key = None, region = None):
        if region is None:
            region = set()
        if p in region: return
        first_call = len(region) == 0
        if first_call and key is None:
            key = lambda x : self.at(x) == self.at(p)
        elif not key(p): return
        region.add(p)
        for a in adj_points(p):
            if self.in_bounds(p):
                self.expand_region(a, key, region)
        if first_call:
            return region
    
    def points(self):
        for y in range(self.h):
            for x in range(self.w):
                p = (y, x)
                yield p, self.at(p)

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

def surrounding_points(p, include_dir = False):
    for dx in -1, 0, 1:
        for dy in -1, 0, 1:
            if dx == 0 and dy == 0: continue
            if include_dir:
                dir1 = None
                dir2 = None
                if dx == 0 or dy == 0:
                    dir1 = Dir.from_delta_pos(dy, dx)
                else:
                    dir1 = Dir.from_delta_pos(dy, 0)
                    dir2 = Dir.from_delta_pos(0, dx)
                yield (p[0] + dy, p[1] + dx), dir1, dir2
            else:
                yield (p[0] + dy, p[1] + dx)

def perimeter(region):
    return sum(1 for p in region for a in adj_points(p) if a not in region)

def sides(region):
    out = 0
    for p in region:
        for a, dy, dx in corner_points(p, True):
            if (a not in region and dy.advance(p) in region and dx.advance(p) in region) or (dy.advance(p) not in region and not dx.advance(p) in region):
                out += 1
    return out