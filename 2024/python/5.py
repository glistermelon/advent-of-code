rules = []
updates = []

rules_parsed = False
for ln in open('../inputs/5.txt'):
    if ln == '\n':
        rules_parsed = True
    elif not rules_parsed:
        rules.append(tuple(int(n) for n in ln.split('|')))
    else:
        updates.append(tuple(int(n) for n in ln.split(',')))

output = 0
for update in updates:
    valid = True
    for rule in rules:
        if rule[0] in update and rule[1] in update and update.index(rule[1]) < update.index(rule[0]):
            valid = False
            break
    if valid:
        output += update[int(len(update) // 2)]
print(output)

# Part 2

updates = [list(u) for u in updates]

def is_valid(update):
    global rules
    broken = []
    for rule in rules:
        if rule[0] in update and rule[1] in update and update.index(rule[1]) < update.index(rule[0]):
            broken.append(rule)
    return len(broken) == 0, broken if len(broken) else None

from functools import cmp_to_key

def comparator(x, y):
    if x == y: return 0
    for rule in rules:
        if rule[0] == x and rule[1] == y:
            return -1
        elif rule[0] == y and rule[1] == x:
            return 1
    return x - y

output = 0
for update in updates:
    valid, broken = is_valid(update)
    if valid: continue
    update = sorted(update, key=cmp_to_key(comparator))
    output += update[int(len(update) // 2)]
print(output)