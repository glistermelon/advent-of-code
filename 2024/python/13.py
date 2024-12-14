import sympy

def solve(x1, x2, y1, y2, tx, ty):
    matrix = [
        [x1, x2, tx],
        [y1, y2, ty]
    ]
    x = sympy.Matrix(matrix).rref()
    x = [b for a in x for b in a]
    x = [x[2], x[5]]
    return tuple(x)

for part1 in True, False:
    x1 = None
    x2 = None
    y1 = None
    y2 = None
    tx = None
    ty = None
    output = 0
    for ln in open('../inputs/13.txt'):
        if ln.strip() == '':
            continue
        ln = ln.replace('X', '').replace('Y', '').replace('+', '').replace('=', '').replace(',', '')
        if ln.startswith('Button A'):
            ln = ln.strip().split()
            x1 = int(ln[2])
            y1 = int(ln[3])
        elif ln.startswith('Button B'):
            ln = ln.strip().split()
            x2 = int(ln[2])
            y2 = int(ln[3])
        elif ln.startswith('Prize'):
            ln = ln.strip().split()
            tx = int(ln[1]) + (0 if part1 else 10000000000000)
            ty = int(ln[2]) + (0 if part1 else 10000000000000)
            a, b = solve(x1, x2, y1, y2, tx, ty)
            if type(a) is sympy.Integer and type(b) is sympy.Integer:
                output += 3 * a + b
    print(output)