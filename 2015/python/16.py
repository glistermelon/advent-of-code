sues = []
for ln in open('../inputs/16.txt'):
    ln = ln.strip().replace(',', '').replace(':', '').split()[2:]
    sues.append({ ln[i] : int(ln[i + 1]) for i in range(0, len(ln), 2) })

real = {
    'children': 3,
    'cats': 7,
    'samoyeds': 2,
    'pomeranians': 3,
    'akitas': 0,
    'vizslas': 0,
    'goldfish': 5,
    'trees': 3,
    'cars': 2,
    'perfumes': 1
}

for part1 in True, False:
    for i, sue in enumerate(sues):
        check = True
        for k, v in sue.items():
            if part1:
                check = real[k] == v
            else:
                if k in ('cats', 'trees'):
                    check = v > real[k]
                elif k in ('pomeranians', 'goldfish'):
                    check = v < real[k]
                else:
                    check = v == real[k]
            if not check:
                break
        if check:
            print(i + 1)