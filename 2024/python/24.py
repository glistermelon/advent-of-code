from types import SimpleNamespace
import functools

inputs = {}
rules = {}

for ln in open('../inputs/24.txt'):
    ln = ln.replace(':', '').split()
    if len(ln) == 2:
        inputs[ln[0]] = int(ln[1])
    elif len(ln) == 5:
        rules[ln[-1]] = SimpleNamespace(gate=ln[1], args=tuple(sorted((ln[0], ln[2]))), wire=ln[-1])

@functools.cache
def eval_wire(wire : str, can_swap = True):
    if wire in inputs:
        return inputs[wire]
    rule = rules[wire]
    arg1 = eval_wire(rule.args[0])
    arg2 = eval_wire(rule.args[1])
    if rule.gate == 'AND':
        return arg1 & arg2
    elif rule.gate == 'OR':
        return arg1 | arg2
    else: # XOR
        return arg1 ^ arg2

def get_wire_name(c : str, n : int):
    return c + ('0' if n < 10 else '') + str(n)

def get_outputs(wire : str):
    return (r for r in rules.values() if wire in r.args)

def swap_rules(r1 : str, r2 : str):
    swap_rules.swaps.add(r1)
    swap_rules.swaps.add(r2)
    m = rules[r1]
    rules[r1] = rules[r2]
    rules[r2] = m
    rules[r1].wire = r1
    rules[r2].wire = r2

swap_rules.swaps = set()

# Part 1

print(int(''.join(str(eval_wire(get_wire_name('z', i))) for i in range(46))[::-1], base=2))

# Part 2 (this code sucks but works for my input)

cin = [r for r in rules.values() if r.gate == 'AND' and r.args == ('x00', 'y00')][0].wire
for i in range(1, 44):
    x = get_wire_name('x', i)
    y = get_wire_name('y', i)
    first_xor = [r for r in rules.values() if r.gate == 'XOR' and r.args == (x, y)][0]
    first_and = [r for r in rules.values() if r.gate == 'AND' and r.args == (x, y)][0]
    if 'OR' in (r.gate for r in get_outputs(cin)):
        swap_rules(cin, first_and.wire)
    elif 'OR' in (r.gate for r in get_outputs(first_xor.wire)):
        swap_rules(first_xor.wire, first_and.wire)
    second_xor = [r for r in rules.values() if r.gate == 'XOR' and first_xor.wire in r.args][0]
    second_and = [r for r in get_outputs(cin) if r.gate == 'AND'][0]
    if 'OR' in (r.gate for r in get_outputs(second_xor.wire)):
        if 'OR' not in (r.gate for r in get_outputs(first_and.wire)):
            swap_rules(second_xor.wire, first_and.wire)
        else:
            swap_rules(second_xor.wire, second_and.wire)
    cout_or = list(get_outputs(second_and.wire))[0]
    if cout_or.wire[0] == 'z':
        swap_rules(cout_or.wire, second_xor.wire)
    cin = cout_or.wire

print(','.join(sorted(list(swap_rules.swaps))))