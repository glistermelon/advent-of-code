from utils import windows

lines = open('../inputs/19.txt').read().splitlines(keepends=False)
target_mol = lines[-1]
replacements = []
for ln in lines[:-2]:
    ln = ln.split()
    in_c = ln[0]
    out = ln[-1]
    replacements.append((in_c, out))

# Part 1

def get_new_mols(mol : str, reverse = False):
    new_mols = set()
    for repl in replacements:
        if reverse:
            repl = tuple(reversed(repl))
        for i, w in enumerate(windows(mol, len(repl[0]))):
            if w != repl[0]: continue
            new_mols.add(''.join(mol[:i] + repl[1] + mol[i + len(w):]))
    return new_mols
print(len(get_new_mols(target_mol)))

# Part 2

prev_len = None
steps = 0
while True:
    for repl in replacements:
        repl = tuple(reversed(repl))
        for i, w in enumerate(windows(target_mol, len(repl[0]))):
            if w != repl[0]: continue
            target_mol = ''.join(target_mol[:i] + repl[1] + target_mol[i + len(w):])
            steps += 1
            break
    if prev_len == len(target_mol): break
    prev_len = len(target_mol)

if target_mol != 'e':
    print('Part 2 failed')
else:
    print(steps)