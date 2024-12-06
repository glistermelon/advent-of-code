from enum import Enum

class Dir(Enum):

    up = 1
    right = 2
    down = 3
    left = 4

    @staticmethod
    def from_char(c):
        return {
            '^': Dir.up,
            '>': Dir.right,
            'v': Dir.down,
            '<': Dir.left
        }[c]

# origin is at top left for Board and Pawn

class Board:

    def __init__(self, width, height): # size is width then height
        self.width = width
        self.height = height
        self.env = [[None] * height] * width

    @staticmethod
    def from_text_map(text : str):
        env = list([list(t) for t in zip(*text.splitlines(keepends=False))])
        board = Board(len(env), len(env[0]))
        board.env = env
        return board
    
    def find_feature(self, feature):
        x = 0
        while x < len(self.env) and feature not in self.env[x]:
            x += 1
        if x == len(self.env): return None
        return x, self.env[x].index(feature)
    
    def feature_at_pawn(self, pawn):
        if not self.pawn_in_bounds(pawn): return None
        return self.env[pawn.pos[0]][pawn.pos[1]]
    
    def set_feature_at_pawn(self, pawn, feature):
        if not self.pawn_in_bounds(pawn): return
        self.env[pawn.pos[0]][pawn.pos[1]] = feature
    
    def pawn_in_bounds(self, pawn):
        x, y = pawn.pos[0], pawn.pos[1]
        return x >= 0 and y >= 0 and x < len(self.env) and y < len(self.env[0])

class Pawn:

    def __init__(self, pos : tuple[int, int], dir : Dir):
        self.pos = pos
        self.dir = dir
    
    def move(self, steps = 1):
        pos = list(self.pos)
        if self.dir == Dir.up: pos[1] -= steps
        elif self.dir == Dir.down: pos[1] += steps
        elif self.dir == Dir.left: pos[0] -= steps
        elif self.dir == Dir.right: pos[0] += steps
        self.pos = tuple(pos)
    
    def turn(self, right = True):
        cycle = [Dir.up, Dir.right, Dir.down, Dir.left, Dir.up]
        if not right: cycle = cycle[::-1]
        self.dir = cycle[cycle.index(self.dir) + 1]
    
    def copy(self):
        return Pawn(self.pos, self.dir)
    
board : Board = Board.from_text_map(open('../inputs/6.txt').read())

def move(pawn):
    pawn.move()
    if board.feature_at_pawn(pawn) == '#':
        pawn.move(-1)
        pawn.turn()

# Part 1

pawn : Pawn = Pawn(board.find_feature('^'), Dir.up)
positions = set()

while True:
    positions.add(pawn.pos)
    move(pawn)
    if not board.pawn_in_bounds(pawn): break

print(len(positions))

# Part 2

pawn = Pawn(board.find_feature('^'), Dir.up)
positions = set()
permutations = set()
obstructions = set()

while True:

    positions.add(pawn.pos)
    permutations.add(pawn.pos + tuple((pawn.dir,)))

    copy = pawn.copy()
    copy.move()
    if board.pawn_in_bounds(copy) and board.feature_at_pawn(copy) == '.' and copy.pos not in obstructions and copy.pos not in positions:
        pos = copy.pos
        board.set_feature_at_pawn(copy, '#')
        copy.move(-1)
        looped = False
        extra_positions = set()
        extra_positions.add(copy.pos + tuple((copy.dir,)))
        while True:
            move(copy)
            if not board.pawn_in_bounds(copy): break
            data = copy.pos + tuple((copy.dir,))
            if data in permutations or data in extra_positions:
                looped = True
                break
            extra_positions.add(data)
        if looped:
            obstructions.add(pos)
        board.env[pos[0]][pos[1]] = '.'

    move(pawn)
    if not board.pawn_in_bounds(pawn): break

print(len(obstructions))