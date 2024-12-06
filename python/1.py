left = []
right = []
for ln in open('../inputs/1.txt'):
    ln = ln.split()
    left.append(int(ln[0]))
    right.append(int(ln[1]))
left.sort()
right.sort()
print(sum(abs(right[i] - left[i]) for i in range(len(left))))
print(sum(x * right.count(x) for x in left))