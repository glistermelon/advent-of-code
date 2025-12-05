from intervaltree import IntervalTree

puzzle_input = open('../inputs/5.txt').read()

fresh = None
test = None

def process_input():

    global puzzle_input
    puzzle_input = puzzle_input.replace('\r', '').split('\n\n')

    global fresh
    fresh = puzzle_input[0].splitlines(keepends=False)
    fresh = [tuple(map(int, s.split('-'))) for s in fresh]
    
    global test
    test = list(map(int, puzzle_input[1].splitlines(keepends=False)))

def solve_part_1():
    return sum(1 for n in test if any(n >= a and n <= b for a, b in fresh))

def solve_part_2():
    tree = IntervalTree()
    for a, b in fresh:
        tree[a:b+1] = True
    tree.merge_overlaps()
    return sum(x.end - x.begin for x in tree)

process_input()
print(solve_part_1())
print(solve_part_2())