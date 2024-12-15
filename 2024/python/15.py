from board_util import *
from util import *

input_data = open('../inputs/15.txt').read()
input_data_split = input_data.find('\n\n')
moves = input_data[input_data_split + 2:].replace('\n', '')

def replace_char(c):
    if c == 'O':
        return '[]'
    if c == '@':
        return '@.'
    return c * 2

def move_box_part1(board : Board, pos : tuple[int, int], dir : Dir, mutate = False):
    new_pos = dir.advance(pos)
    if board[new_pos] == '#': return True
    if board[new_pos] != '.' and move_box_part1(board, new_pos, dir, mutate): return True
    if mutate:
        board[new_pos] = board[pos]
        board[pos] = '.'
    return False

def move_box_part2(board : Board, pos : tuple[int, int], dir : Dir, mutate = False):
    l_pos, r_pos = None, None
    if board[pos] == board[Dir.right.advance(pos)]:
        l_pos, r_pos = pos, Dir.right.advance(pos)
    else:
        l_pos, r_pos = Dir.left.advance(pos), pos
    new_pl = dir.advance(l_pos)
    new_pr = dir.advance(r_pos)
    for p in new_pl, new_pr:
        if board[p] == '#': return True
        if p not in (l_pos, r_pos) and board[p] != '.' and move_box_part2(board, p, dir, mutate): return True
    if mutate:
        box_id = board[l_pos]
        for p in l_pos, r_pos:
            board[p] = '.'
        for p in new_pl, new_pr:
            board[p] = box_id
    return False

for part1 in True, False:

    board_data = [
        [c for c in ln] if part1 else [cr for c in ln for cr in replace_char(c)]
        for ln in input_data[:input_data_split].splitlines(keepends=False)
    ]
    board = Board(board_data)

    if not part1:
        for i, pl in enumerate(find_nd(board.arr, '[')):
            board[pl] = i
            board[Dir.right.advance(pl)] = i

    robot = find_nd(board.arr, '@')[0]

    move_box = move_box_part1 if part1 else move_box_part2

    for move in moves:
        dir = Dir.from_arrow_char(move)
        board[robot] = '.'
        robot = dir.advance(robot)
        if board[robot] == '#' or (board[robot] != '.' and move_box(board, robot, dir)):
            robot = dir.advance(robot, -1)
        elif board[robot] != '.':
            move_box(board, robot, dir, True)
        board[robot] = '@'

    output = 0
    for y in range(board.h):
        for x, v in enumerate(board.arr[y]):
            if (part1 and v == 'O') or ((not part1) and type(v) is int and v == board[(y, x + 1)]):
                output += 100 * y + x
    print(output)