from board_util import *

for part1 in True, False:
    board = Board(open('../inputs/18.txt').read().splitlines(keepends=False))
    for step in range(100):
        new_board = board.copy()
        for p, s in board.points():
            neighbors = sum(1 for a in surrounding_points(p) if board[a] == '#')
            if s == '#':
                if neighbors not in (2, 3):
                    new_board[p] = '.'
            else:
                if neighbors == 3:
                    new_board[p] = '#'
        if not part1:
            for y in 0, board.h - 1:
                for x in 0, board.w - 1:
                    new_board[(y, x)] = '#'
        board = new_board
    print(sum(1 for _, s in board.points() if s == '#'))