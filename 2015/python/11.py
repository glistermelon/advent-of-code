import base36
from util import windows, deltas

def unsafe(pw : str):

    for c in 'iol':
        if c in pw:
            return True
        
    check = 0
    skip = False
    for w in windows(pw, 2):
        if skip:
            skip = False
            continue
        if w[0] == w[1]:
            check += 1
            if check == 2:
                break
            skip = True
    if check != 2: return True

    check = False
    for w in windows(pw, 3):
        if list(deltas(ord(c) for c in w)) == [1, 1]:
            check = True
            break
    return not check

def get_next_pw(pw : str):
    pw = [c for c in pw][::-1]
    while True:
        for i, c in enumerate(pw):
            if c == 'z':
                pw[i] = 'a'
                continue
            pw[i] = chr(ord(c) + 1)
            break
        if not unsafe(''.join(pw[::-1])):
            break
    return ''.join(pw[::-1])

pw = get_next_pw(open('../inputs/11.txt').read())
print(pw)
print(get_next_pw(pw))