from itertools import combinations

buckets = list(map(int, open('../inputs/17.txt').read().splitlines(keepends=False)))

total_combos = 0
minimum = None
minimal_combos = 0
for i in range(2, len(buckets)):
    for c in combinations(buckets, i):
        if sum(c) == 150:
            total_combos += 1
            if minimum is None:
                minimum = i
            if minimum == i:
                minimal_combos += 1
print(total_combos)
print(minimal_combos)