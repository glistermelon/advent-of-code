from board_util import *
from utils import *
import sys

sys.setrecursionlimit(10000) # Just for setting up nodes because I'm lazy

board = Board(open('../inputs/16.txt').read().splitlines(keepends=False))

class Vertex:

    vertices = []

    def __init__(self, pos : tuple[int, int]):
        self.pos = pos
        self.adj = [None] * 4
        Vertex.vertices.append(self)
    
    def __repr__(self):
        return 'V' + str(self.pos)
    
    def add_adj(self, v, dir : Dir):
        self.adj[dir.value] = v
        v.adj[dir.flip().value] = self
    
    def get_adj(self, dir : Dir):
        return self.adj[dir.value]
    
    def has_adj(self, dir : Dir):
        return self.get_adj(dir) is not None
    
    @staticmethod
    def at(pos : tuple[int, int]):
        for v in Vertex.vertices:
            if v.pos == pos:
                return v
        return None

end_pos = find_nd(board.arr, 'E')[0]

def create_vertex(init_p : tuple[int, int]):
    vertex = Vertex(init_p)
    for dir in Dir.up, Dir.right, Dir.down, Dir.left:
        p = init_p
        while True:
            p = dir.advance(p)
            if board[p] == '#': break
            if p == end_pos or board[dir.turn_left().advance(p)] != '#' or board[dir.turn_right().advance(p)] != '#':
                existing = Vertex.at(p)
                if existing:
                    vertex.add_adj(existing, dir) # can this line be removed
                else:
                    vertex.add_adj(create_vertex(p), dir)
                break
    return vertex

def dir_between_vertices(v1 : Vertex, v2 : Vertex):
    return Dir.from_delta_pos(v2.pos[0] - v1.pos[0], v2.pos[1] - v1.pos[1])

def tiles_between(v1 : Vertex | tuple[int, int], v2 : Vertex | tuple[int, int]):
    p1 = v1.pos if type(v1) is Vertex else v1
    p2 = v2.pos if type(v2) is Vertex else v2
    tiles = set()
    tiles.add(p1)
    dir = Dir.from_delta_pos(p2[0] - p1[0], p2[1] - p1[1])
    while p1 != p2:
        p1 = dir.advance(p1)
        tiles.add(p1)
    return tiles

def solve(start_vertex : Vertex, end_vertex : Vertex):
    scores = { start_vertex: 0 }
    visited = set()
    directions = { start_vertex: Dir.right }
    vtx = start_vertex
    dir = Dir.right
    score = 0
    while vtx is not end_vertex:
        visited.add(vtx)
        for d in dir, dir.turn_left(), dir.turn_right():
            if vtx.has_adj(d):
                a = vtx.get_adj(d)
                s = score + dist_taxi(vtx.pos, a.pos)
                if d != dir:
                    s += 1000
                if a not in scores or s < scores[a]:
                    scores[a] = s
                    directions[a] = d
        vtx, score = min(list(i for i in scores.items() if i[0] not in visited), key=lambda x : x[1])
        dir = directions[vtx]

    tiles = set()
    queue = [(end_vertex, dir)]
    while queue:
        vtx, dir = queue.pop(0)
        for a in vtx.adj:
            if a is None or a not in visited:
                continue
            s = dist_taxi(vtx.pos, a.pos)

            if directions[a] != dir_between_vertices(a, vtx):
                s += 1000
            if dir_between_vertices(a, vtx) != dir:
                s += 1000
            if directions[vtx] != dir:
                s -= 1000

            if scores[a] + s == scores[vtx]:
                queue.append((a, dir_between_vertices(a, vtx)))
                tiles.update(tiles_between(vtx, a))

    return score, tiles

start_vertex = create_vertex(find_nd(board.arr, 'S')[0])
end_vertex = Vertex.at(end_pos)
min_score, tiles = solve(start_vertex, end_vertex)
print(min_score)
print(len(tiles))