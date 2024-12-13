from types import SimpleNamespace

class Reindeer:

    def __init__(self, speed : int, run_time : int, rest_time : int):
        self.speed = speed
        self.run_time = run_time
        self.rest_time = rest_time
        self.score = 0
        self.pos = 0
        self.running = run_time
        self.resting = rest_time - 1

reindeer = []

for ln in open('../inputs/14.txt'):
    ln = ln.split()
    reindeer.append(Reindeer(int(ln[3]), int(ln[6]), int(ln[-2])))

for time in range(2503):
    for r in reindeer:
        if r.running:
            r.pos += r.speed
            r.running -= 1
        elif r.resting:
            r.resting -= 1
        else:
            r.running = r.run_time
            r.resting = r.rest_time - 1
    max_pos = max(r.pos for r in reindeer)
    for r in reindeer:
        if r.pos == max_pos:
            r.score += 1

print(max(r.pos for r in reindeer))
print(max(r.score for r in reindeer))