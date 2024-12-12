# Part 1

safe = 0
for report in open('../inputs/2.txt'):
    report = [int(n) for n in report.split()]
    report = [report[i] - report[i - 1] for i in range(1, len(report))]
    if min(report) < 0 and max(report) > 0: continue
    report = [abs(n) for n in report]
    if min(report) == 0 or max(report) > 3: continue
    safe += 1
print(safe)

# Part 2 lazy solution

def check(report : list[int]):
    report = list(report)
    report = [report[i] - report[i - 1] for i in range(1, len(report))]
    if min(report) < 0 and max(report) > 0: return False
    report = [abs(n) for n in report]
    if min(report) == 0 or max(report) > 3: return False
    return True

safe = 0
for report in open('../inputs/2.txt'):
    report = [int(n) for n in report.split()]
    if check(report): safe += 1
    else:
        for i in range(len(report)):
            if check(report[:i] + report[i + 1:]):
                safe += 1
                break
print(safe)