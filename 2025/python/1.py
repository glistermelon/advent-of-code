puzzle_input = open('../inputs/1.txt').read()

pos = 50
pw = 0
for instr in puzzle_input.splitlines(keepends=False):
    sign = 1 if instr[0] == 'R' else -1
    delta = int(instr[1:])
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

print(pw)