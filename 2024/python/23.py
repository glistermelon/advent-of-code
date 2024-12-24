connections = [ln.split('-') for ln in open('../inputs/23.txt').read().splitlines(keepends=False)]
all_nodes = set((n for c in connections for n in c))
connections = { n : set(c[0] if c[0] != n else c[1] for c in connections if n in c) for n in all_nodes }

polys = set(tuple(sorted((node, con))) for node in all_nodes for con in connections[node])
solution = None
while polys:
    solution = next(iter(polys))

    if len(solution) == 3: # for Part 1
        print(sum(1 for p in polys if 't' in (s[0] for s in p)))

    next_polys = set()
    for poly in polys:
        for p in connections[poly[0]].intersection(*(connections[p] for p in poly[1:])):
            next_polys.add(tuple(sorted(poly + (p,))))
    polys = next_polys
print(','.join(solution))