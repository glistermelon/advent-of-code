puzzle_input = open('../inputs/2.txt').read()

ranges = []

def process_input():
    for r in puzzle_input.split(','):
        ranges.append(tuple(map(int, r.strip().split('-'))))

def solve_part_1():
    result = 0
    for r1, r2 in ranges:
        for n in range(r1, r2 + 1):
            s = str(n)
            i = len(s) // 2
            if s[:i] == s[i:]:
                result += n
    return result

def solve_part_2():

    def is_repeating(s: str):
        for l in range(1, len(s) // 2 + 1):
            if all(s[i : i + l] == s[:l] for i in range(0, len(s), l)):
                return True
        return False

    result = 0
    for r1, r2 in ranges:
        for n in range(r1, r2 + 1):
            if is_repeating(str(n)):
                result += n
    return result

process_input()
print(solve_part_1())
print(solve_part_2())