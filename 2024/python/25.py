import itertools

locks = []
keys = []

schematic = []
for ln in open('../inputs/25.txt').read().splitlines(keepends=False) + [None]:
    if ln:
        schematic.append(ln)
    else:
        (locks if '#' in schematic[0] else keys).append(tuple(r.count('#') - 1 for r in zip(*schematic)))
        schematic = []

print(sum(1 for key, lock in itertools.product(keys, locks) if all(kh + lh <= 5 for kh, lh in zip(key, lock))))