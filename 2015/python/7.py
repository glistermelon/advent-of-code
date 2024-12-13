import functools

rules = {}
for rule in open('../inputs/7.txt').read().splitlines(keepends=False):
    rule = rule.split()
    gate = None
    args = None
    if rule[0] == 'NOT':
        gate = 'NOT'
        args = rule[1]
    elif len(rule) == 3:
        gate = None
        args = rule[0]
    else:
        gate = rule[1]
        args = [rule[0], (rule[2] if 'SHIFT' not in gate else int(rule[2]))]
        for i, s in enumerate(args):
            try:
                args[i] = int(s)
            except:
                pass
        args = tuple(args)
    if type(args) is str:
        try:
            args = int(args)
        except:
            pass
    rules[rule[-1]] = gate, args

first_17bit_number = 2**16
max_16bit_number = first_17bit_number - 1

@functools.cache
def solve(wire):
    if type(wire) is int: return wire
    gate, arg = rules[wire]
    if gate is None:
        return arg if type(arg) is int else solve(arg)
    elif gate == 'NOT':
        return max_16bit_number - solve(arg)
    elif gate == 'AND':
        return solve(arg[0]) & solve(arg[1])
    elif gate == 'OR':
        return solve(arg[0]) | solve(arg[1])
    elif gate == 'LSHIFT':
        return (solve(arg[0]) << arg[1]) % first_17bit_number
    elif gate == 'RSHIFT':
        return (solve(arg[0]) >> arg[1]) % first_17bit_number

a = solve('a')
print(a)
rules['b'] = (None, a)
solve.cache_clear()
print(solve('a'))