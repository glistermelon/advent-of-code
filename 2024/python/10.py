from util import find_nd

board = [[int(c) for c in ln] for ln in open('../inputs/10.txt').read().splitlines(keepends=False)]

part2 = False

def search(nodes : list[(int, int)]):
    y, x = nodes[-1]
    if board[y][x] == 9:
        return 1 if part2 else set(((y, x),))
    ret = 0 if part2 else set()
    for dx in (-1, 0, 1):
        for dy in (-1, 0, 1):
            if dx != 0 and dy != 0: continue
            n1 = (y + dy, x + dx)
            if n1[0] < 0 or n1[1] < 0 or n1[0] >= len(board) or n1[1] >= len(board[0]): continue
            if board[n1[0]][n1[1]] - board[y][x] != 1: continue
            if n1 in nodes: continue
            if board[n1[0]][n1[1]] - board[y][x] == 1:
                nodes.append(n1)
                if part2:
                    ret += search(nodes)
                else:
                    ret.update(search(nodes))
                nodes.pop()
    return ret

print(sum(len(search([(y, x)])) for y, x in find_nd(board, 0)))
part2 = True
print(sum(search([(y, x)]) for y, x in find_nd(board, 0)))
