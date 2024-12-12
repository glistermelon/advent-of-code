from types import SimpleNamespace
from enum import Enum

class InstructionType(Enum):

    off = 0
    on = 1
    toggle = 2

class Instruction:

    def __init__(self, str):

        if str.startswith('toggle'):
            self.type = InstructionType.toggle
        elif str.startswith('turn on'):
            self.type = InstructionType.on
        else:
            self.type = InstructionType.off
        str = str.split()
        self.tl = tuple(int(n) for n in str[-3].split(','))
        self.br = tuple(int(n) for n in str[-1].split(','))

def p_gte(p0 : tuple[int, int], p1 : tuple[int, int]):
    return p0[0] >= p1[0] and p0[1] >= p1[1]

def p_lte(p0 : tuple[int, int], p1 : tuple[int, int]):
    return p0[0] <= p1[0] and p0[1] <= p1[1]

instructions = []
for ln in open('../inputs/6.txt'):
    instructions.append(Instruction(ln))

# This is terrible but I don't find this problem interesting enough to spend time optimizing

for part1 in True, False:
    output = 0
    for x in range(1000):
        for y in range(1000):
            p = (x, y)
            state = False if part1 else 0
            for instr in instructions:
                if p_gte(p, instr.tl) and p_lte(p, instr.br):
                    if instr.type == InstructionType.off:
                        state = False if part1 else max(state - 1, 0)
                    elif instr.type == InstructionType.on:
                        state = True if part1 else state + 1
                    else:
                        state = not state if part1 else state + 2
            if part1 and state:
                output += 1
            elif not part1:
                output += state
    print(output)