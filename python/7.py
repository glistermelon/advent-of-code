nums = None
part2 = False

from math import log, ceil, log2, floor

log2_10 = log2(10)

def solve(idx : int, targ : int):
    if idx == 0: return nums[0] == targ
    if part2:
        unconcat, preconcat = divmod(targ, 10**ceil(0.0000001 + log2(nums[idx]) / log2_10))
        if preconcat == nums[idx] and solve(idx - 1, unconcat): return True
    div, mod = divmod(targ, nums[idx])
    if mod == 0 and solve(idx - 1, div): return True
    if nums[idx] < targ and solve(idx - 1, targ - nums[idx]): return True
    return False

output1 = 0
output2 = 0
for ln in open('../inputs/7.txt'):
    ln = ln.split(':')
    targ = int(ln[0])
    nums = [int(n) for n in ln[1].strip().split()]
    if solve(len(nums) - 1, targ):
        output1 += targ
    else:
        part2 = True
        if solve(len(nums) - 1, targ):
            output2 += targ
        part2 = False

print(output1)
print(output1 + output2)