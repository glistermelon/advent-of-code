from util import *

output1 = 0
output2 = 0
for ln in open('../inputs/2.txt'):
    dim = [int(n) for n in ln.split('x')]
    sides = [dim[i] * dim[j] for i in range(1, 3) for j in range(i)]
    output1 += 2 * sum(sides) + min(sides)
    output2 += prod(dim)
    dim.remove(max(dim))
    output2 += 2 * sum(dim)
print(output1)
print(output2)