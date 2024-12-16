from util import prod
import itertools

packages = list(map(int, open('../inputs/24.txt').read().splitlines(keepends=False)))

for num_groups in 3, 4:
    group_weight = sum(packages) // num_groups
    quantum_entanglement = None
    for i in range(1, len(packages) - 1):
        c = list(prod(c) for c in itertools.combinations(packages, i) if sum(c) == group_weight)
        if not c: continue
        quantum_entanglement = min(c)
        break
    print(quantum_entanglement)