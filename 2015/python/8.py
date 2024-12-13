import json

for part1 in True, False:
    output = 0
    for ln in open('../inputs/8.txt'):
        ln = ln.strip()
        s = None
        exec('s = ' + ln)
        output += abs(len(ln) - len(s if part1 else json.dumps(ln)))
    print(output)