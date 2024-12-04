# Part 1

lines = [ln.strip() for ln in open('inputs/4.txt').readlines()]

if len(lines) != len(lines[0]): exit()

horizontal = list(lines)
vertical = [''.join(lines[j][i] for j in range(len(lines))) for i in range(len(lines[0]))]
diag1 = [''.join(lines[i][i + x] for i in range(len(lines)) if i + x >= 0 and i + x < len(lines)) for x in range(1 - len(lines), len(lines))]
diag2 = [''.join(lines[i][len(lines) - i - 1 + x] for i in range(len(lines)) if len(lines) - i - 1 + x >= 0 and len(lines) - i - 1 + x < len(lines)) for x in range(1 - len(lines), len(lines))]

def count(string, reverse = False):
    c = 0
    sub = 'XMAS'
    if reverse:
        sub = sub[::-1]
    for i in range(len(string) - len(sub) + 1):
        if string[i : i + len(sub)] == sub:
            c += 1
    return c

output = 0
for wordlist in (horizontal, vertical, diag1, diag2,):
    for word in wordlist:
        output += count(word)
        output += count(word, True)

print(output)

# Part 2

output = 0
for r in range(1, len(lines) - 1):
    for c in range(1, len(lines[r]) - 1):
        if lines[r][c] == 'A' and (lines[r - 1][c - 1] + lines[r + 1][c + 1] in ('MS', 'SM',)) and (lines[r - 1][c + 1] + lines[r + 1][c - 1] in ('MS', 'SM',)):
            output += 1
print(output)