puzzle_input = open('../inputs/3.txt').read()

banks = None

def process_input():
    global banks
    banks = [tuple(map(int, ln)) for ln in puzzle_input.splitlines(keepends=False)]

def optimize_bank(bank: tuple[int], num_batteries: int):
    joltage = 0
    digit_i = 0
    for remaining in range(num_batteries - 1, -1, -1):
        digit = max(bank[digit_i:-remaining if remaining != 0 else None])
        digit_i = bank.index(digit, digit_i) + 1
        joltage += digit * 10**remaining
    return joltage

def solve_part_1():
    return sum(optimize_bank(bank, 2) for bank in banks)

def solve_part_2():
    return sum(optimize_bank(bank, 12) for bank in banks)

process_input()
print(solve_part_1())
print(solve_part_2())