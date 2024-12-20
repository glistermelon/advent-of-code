import functools

def read_input():
    f = open('../inputs/19.txt')
    subpatterns = f.readline().strip().split(', ')
    f.readline()
    patterns = [ln.strip() for ln in f]
    return patterns, sorted(subpatterns, key=lambda p : len(p), reverse=True)

patterns, subpatterns = read_input()

@functools.cache
def is_possible(pattern : str):
    if not pattern:
        return True
    for s in subpatterns:
        if pattern.startswith(s) and is_possible(pattern[len(s):]):
            return True
    return False

@functools.cache
def permutations(pattern : str):
    if not pattern:
        return 1
    out = 0
    for s in subpatterns:
        if pattern.startswith(s):
            out += permutations(pattern[len(s):])
    return out

print(sum(1 for p in patterns if is_possible(p)))
print(sum(permutations(p) for p in patterns))


