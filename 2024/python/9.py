from utils import find_with_key

def get_disk(include_zero):
    return [[i // 2, int(c)] if i % 2 == 0 else int(c) for i, c in enumerate(open('../inputs/9.txt').read()) if include_zero or c != '0']

def checksum(disk):
    return sum(i * n for i, n in enumerate(0 if type(block) is int else block[0] for block in disk for _ in range(block if type(block) is int else block[1])))

# Part 1

disk = get_disk(True)
empty_i = 1
while empty_i < len(disk):
    if type(disk[-1]) is int:
        disk.pop()
        continue
    move = min(disk[empty_i], disk[-1][1])
    disk.insert(empty_i, (disk[-1][0], move))
    empty_i += 1
    if disk[empty_i] == move:
        disk.pop(empty_i)
        empty_i += 1
    else:
        disk[empty_i] -= move
    if disk[-1][1] == move:
        disk.pop()
    else:
        disk[-1][1] -= move
print(checksum(disk))

# Part 2

disk = get_disk(False)
block_i = len(disk) - 1
while block_i > 1:
    block = disk[block_i]
    if type(block) is int:
        block_i -= 1
        continue
    empty_i = find_with_key(disk, lambda v : type(v) is int and v >= block[1])
    if empty_i == -1 or empty_i > block_i:
        block_i -= 1
        continue
    disk[block_i] = block[1]
    if disk[empty_i] == block[1]:
        disk.pop(empty_i)
        block_i -= 1
    else:
        disk[empty_i] -= block[1]
    disk.insert(empty_i, block)
print(checksum(disk))