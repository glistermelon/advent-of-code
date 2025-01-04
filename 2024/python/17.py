from utils import *

# This is hardcoded to my input due to a combination of laziness and lack of other inputs to test

instructions = [2, 4, 1, 2, 7, 5, 4, 1, 1, 3, 5, 5, 0, 3, 3, 0]

def get_combo(registers : dict[str, int], n : int):
    if n <= 3:
        return n
    return registers['ABC'[n - 4]]

def evaluate(init_a : int, first_only : bool = False):
    ip = 0
    registers = dict(A=init_a, B=0, C=0)
    registers['A'] = init_a
    out = []
    while ip >= 0 and ip < len(instructions):
        if out and first_only:
            return out[0]
        opcode = instructions[ip]
        operand = instructions[ip + 1]
        if opcode == 0: # adv
            registers['A'] = registers['A'] // 2**get_combo(registers, operand)
        elif opcode == 1: # bxl
            registers['B'] = registers['B'] ^ operand
        elif opcode == 2: # bst
            registers['B'] = get_combo(registers, operand) % 8
        elif opcode == 3: # jnz
            if registers['A'] != 0:
                ip = operand
                continue
        elif opcode == 4: # bxc
            registers['B'] = registers['B'] ^ registers['C']
        elif opcode == 5: # out
            out.append(get_combo(registers, operand) % 8)
        elif opcode == 6: # bdv
            registers['B'] = registers['A'] // 2**get_combo(registers, operand)
        elif opcode == 7: # cdv
            registers['C'] = registers['A'] // 2**get_combo(registers, operand)
        ip += 2
    return out if not first_only else out[0]

sols = set()
sols.add((0, 0))
for triad in instructions[::-1]:
    postc_b = triad ^ 1
    next_sols = set()
    for c in range(8):
        prec_b = postc_b ^ c
        n = (prec_b | (c << (prec_b ^ 2)))
        nr = (0b111 | (0b111 << (prec_b ^ 2)))
        for s, sr in sols:
            s <<= 3
            sr <<= 3
            k = sr & nr
            if s & k == n & k:
                s1 = s | n
                if (s1 >> (prec_b ^ 2)) % 8 == c and s1 % 8 == prec_b:
                    next_sols.add((s1, sr | nr))
    sols = next_sols

print(','.join(map(str, evaluate(27575648))))
print(min(s[0] for s in sols))