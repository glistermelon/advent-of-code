targ_r, targ_c = tuple(map(int, open('../inputs/25.txt').read()[:-1].replace('column', '').replace(',', '').split()[15:17]))

r, c = 1, 1
v = 20151125

while r != targ_r or c != targ_c:

    r -= 1
    c += 1
    if r == 0:
        r = c
        c = 1

    v = (v * 252533) % 33554393

print(v)