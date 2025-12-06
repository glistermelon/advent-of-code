from itertools import groupby

puzzle_input = open('../inputs/6.txt').read()

def prod(i):
    i = iter(i)
    p = next(i)
    while (x := next(i, None)) is not None:
        p *= x
    return p

def split_iter(i, d):
    for empty, g in groupby(i, key=lambda g : g == d):
        if not empty:
            yield tuple(g)

def eval_sheet(sheet):
    return sum(sum(col[:-1]) if col[-1] == '+' else prod(col[:-1]) for col in sheet)

def solve_part_1():
    return eval_sheet(
        tuple(zip(*(map(int, ln.split()) if ln.lstrip()[0].isdigit() else ln.split() for ln in puzzle_input.splitlines(keepends=False))))
    )

def solve_part_2():
    cols = (''.join(chars).strip() for chars in zip(*puzzle_input.splitlines(keepends=False)))
    sheet = [[int(b[0][:-1].rstrip())] + list(map(int, b[1:])) + [b[0][-1]] for b in split_iter(cols, '')]
    return eval_sheet(sheet)

print(solve_part_1())
print(solve_part_2())