import numpy

data = []

for ln in open('../inputs/15.txt'):
    ln = ln.replace(',', '').split()
    data.append(tuple(int(ln[i]) for i in range(2, 11, 2)))

data = numpy.array(data).transpose()

limit = 100

for part1 in True, False:
    max_prod = 0
    for x1 in range(limit + 1):
        for x2 in range(limit + 1 - x1):
            for x3 in range(limit + 1 - x1 - x2):
                x4 = limit - x1 - x2 - x3
                v = numpy.array([x1, x2, x3, x4])
                p = numpy.dot(data, v)
                if (not part1) and p[4] != 500:
                    continue
                p[p < 0] = 0
                p = numpy.prod(p[:4])
                if p > max_prod:
                    max_prod = p
    print(max_prod)