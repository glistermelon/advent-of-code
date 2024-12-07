def base3(n):
    out = ''
    while n:
        n, rem = divmod(n, 3)
        out = str(rem) + out
    return out

def solve(nums, opps):
    nums = list(nums)
    while len(opps):
        if opps[0] == '0':
            nums[0] += nums.pop(1)
        elif opps[0] == '1':
            nums[0] *= nums.pop(1)
        elif opps[0] == '2':
            nums[0] = int(str(nums[0]) + str(nums.pop(1)))
        opps = opps[1:]
    return nums[0]

outputs = [0, 0]
for ln in open('../inputs/7.txt'):
    ln = ln.split(':')
    target = int(ln[0])
    nums = [int(n) for n in ln[1].strip().split()]
    num_opps = len(nums) - 1
    for part in range(2):
        worked = False
        for pattern in range((3 if part else 2)**(num_opps)):
            pattern = base3(pattern) if part else bin(pattern)[2:]
            pattern = ('0' * (num_opps - len(pattern))) + pattern
            if solve(nums, pattern) == target:
                worked = True
                break
        if worked:
            outputs[part] += target

for output in outputs:
    print(output)