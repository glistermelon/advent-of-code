from util import find_nd

board = [[int(c) for c in ln] for ln in open('../inputs/10.txt').read().splitlines(keepends=False)]

part2 = False

def search(done : list[(int, int)] = []):
    y, x = done[-1]
    if board[y][x] == 9:
        return 1 if part2 else set(((y, x),))
    ret = 0 if part2 else set()
    for dx in (-1, 0, 1):
        for dy in (-1, 0, 1):
            if dx != 0 and dy != 0:
                continue
            p1 = (y + dy, x + dx)
            if p1[0] < 0 or p1[1] < 0 or p1[0] >= len(board) or p1[1] >= len(board[0]):
                continue
            if p1 in done:
                continue
            if board[p1[0]][p1[1]] - board[y][x] == 1 and p1 not in done:
                done.append(p1)
                if part2:
                    ret += search(done)
                else:
                    ret.update(search(done))
                done.pop()
    return ret

print(sum(len(search([(y, x)])) for y, x in find_nd(board, 0)))
part2 = True
print(sum(search([(y, x)]) for y, x in find_nd(board, 0)))