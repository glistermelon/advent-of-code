puzzle_input = open('../inputs/1.txt').read()

instructions = None

def process_input():
    global instructions
    instructions = []
    for ln in puzzle_input.splitlines(keepends=False):
        sign = 1 if ln[0] == 'R' else -1
        delta = int(ln[1:])
        instructions.append((sign, delta))

def solve_part_1():
    pos = 50
    pw = 0
    for sign, delta in instructions:
        pos = (pos + sign * delta) % 100
        if pos == 0:
            pw += 1
    return pw

def solve_part_2():
    pos = 50
    pw = 0
    for sign, delta in instructions:
        pos_og_zero = pos == 0
        pos += sign * delta
        pw_delta, pos = divmod(pos, 100)
        pw_delta = abs(pw_delta)
        if sign == -1:
            if pos_og_zero:
                pw_delta -= 1
            if pos == 0:
                pw_delta += 1
        pw += pw_delta
    return pw

process_input()
print(solve_part_1())
print(solve_part_2())