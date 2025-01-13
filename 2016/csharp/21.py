import re

SWAP = re.compile("swap position (\\d+) with position (\\d+)")
SWAP_LETTER = re.compile("swap letter ([a-z]) with letter ([a-z])")
REVERSE = re.compile("reverse positions (\\d+) through (\\d+)")
ROTATE = re.compile("rotate (left|right) (\\d+) step")
MOVE = re.compile("move position (\\d+) to position (\\d+)")
ROTATE_BASED = re.compile("rotate based on position of letter ([a-z])")


def _swap(inp, p1, p2, rev=False):
    p1, p2 = map(int, (p1, p2))
    inp[p2], inp[p1] = inp[p1], inp[p2]
    return inp


def _swap_letter(inp, p1, p2, rev=False):
    p1 = inp.index(p1)
    p2 = inp.index(p2)
    return _swap(inp, p1, p2)


def _reverse(inp, p1, p2, rev=False):
    p1, p2 = map(int, (p1, p2))
    inp[p1:p2 + 1] = list(reversed(inp[p1:p2 + 1]))
    return inp


def _rotate(inp, lr, p2, rev=False):
    p2 = int(p2)
    p2 = p2 % len(inp)

    if rev:
        if lr == "left":
            lr = "right"
        else:
            lr = "left"

    if lr == "left":
        inp = inp[p2:] + inp[:p2]
    else:
        inp = inp[-p2:] + inp[:-p2]
    return inp


def _move(inp, p1, p2, rev=False):
    p1, p2 = map(int, (p1, p2))
    if rev:
        p1, p2 = p2, p1
    val = inp[p1]
    inp = inp[:p1] + inp[p1 + 1:]
    inp.insert(p2, val)
    return inp


def _rotate_based(inp, letter, rev=False):
    if rev:
        for i in range(1, len(inp) + 1):
            tryval = _rotate(list(inp), "left", i)
            if _rotate_based(tryval, letter) == inp:
                return tryval
        return None

    pos = inp.index(letter)
    if pos >= 4:
        pos += 1
    return _rotate(inp, "right", pos + 1)


OP = {
    SWAP: _swap,
    SWAP_LETTER: _swap_letter,
    REVERSE: _reverse,
    ROTATE: _rotate,
    MOVE: _move,
    ROTATE_BASED: _rotate_based
}


def solve_reverse(inp, seq_reversed):
    inp = list(inp)
    for s in seq_reversed:
        for rx, cb in OP.items():
            args = rx.findall(s)
            if args:
                inp = cb(inp, *(list(args[0]) + [True]))
    return "".join(inp)


def solve(inp, seq):
    inp = list(inp)
    for s in seq:
        for rx, cb in OP.items():
            args = rx.findall(s)
            if args:
                inp = cb(inp, *args[0])
                print(''.join(inp))
    return "".join(inp)

if __name__ == '__main__':
    with open("2016/inputs/21.txt") as inp:
        inp = list(inp)
        solve("abcdefgh", inp)