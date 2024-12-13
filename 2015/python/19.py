from util import windows

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

# Part 2 todo