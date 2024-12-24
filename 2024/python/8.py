from utils import find_nd, piecewise
from itertools import combinations

board_str = open('../inputs/8.txt').read()
board = board_str.splitlines(keepends=False)

antennae = lambda : (combinations(find_nd(board, a), 2) for a in set(board_str) if a != '.' and board_str.count(a) != 1)

antinodes = set()
for combos in antennae():
    for a1, a2 in combos:
        delta = piecewise(a1, a2, lambda n1, n2 : n1 - n2)
        a1 = piecewise(a1, delta, lambda n1, n2 : n1 + n2)
        a2 = piecewise(a2, delta, lambda n1, n2 : n1 - n2)
        for a in (a1, a2):
            if a[0] >= 0 and a[1] >= 0 and a[0] < len(board) and a[1] < len(board[0]):
                antinodes.add(a)
print(len(antinodes))

antinodes = set()
for combos in antennae():
    for a1, a2 in combos:
        dx = a1[1] - a2[1]
        dy = a1[0] - a2[0]
        antinodes |= set(
            piecewise(a1, (dy, dx), lambda n1, n2 : n1 + n2 * i) for i in range(1 + min(
                1 if dx == 0 else (len(board[0]) - a1[1] - 1 if dx > 0 else -a1[1]) // dx,
                1 if dy == 0 else (len(board) - a1[0] - 1 if dy > 0 else -a1[0]) // dy
            ))
        )
        antinodes |= set(
            piecewise(a2, (dy, dx), lambda n1, n2 : n1 - n2 * i) for i in range(1 + min(
                1 if dx == 0 else (a2[1] if dx > 0 else -len(board[0]) + a2[1] + 1) // dx,
                1 if dy == 0 else (a2[0] if dy > 0 else -len(board) + a2[0] + 1) // dy
            ))
        )
print(len(antinodes))