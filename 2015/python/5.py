from utils import windows

def nice1(string : str):
    if sum(string.count(c) for c in 'aeiou') < 3: return False
    pair_found = False
    for w in windows(string, 2):
        if w[0] == w[1]:
            pair_found = True
            break
    if not pair_found: return False
    for s in 'ab', 'cd', 'pq', 'xy':
        if s in string:
            return False
    return True

def nice2(string : str):
    check = False
    for i, w in enumerate(windows(string, 2)):
        if w in string[i + 2:]:
            check = True
            break
    if not check: return False
    check = False
    for w in windows(string, 3):
        if w[0] == w[2] and w[0] != w[1]:
            check = True
            break
    return check

lines = open('../inputs/5.txt').read().splitlines(keepends=False)
print(sum(int(nice1(ln)) for ln in lines))
print(sum(int(nice2(ln)) for ln in lines))