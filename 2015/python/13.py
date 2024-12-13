connections = {}
people = set()

for ln in open('../inputs/13.txt'):
    ln = ln.split()
    pair = (ln[0], ln[-1][:-1])
    rev_pair = tuple(reversed(pair))
    change = int(ln[3]) * (1 if ln[2] == 'gain' else -1)
    if rev_pair in connections:
        connections[rev_pair] += change
        connections[pair] = connections[rev_pair]
    else:
        connections[pair] = change
    people.add(ln[0])

def search(seated, total_change = 0):
    last_person = len(seated) == len(people) - 1
    latest = seated[-1]
    greatest_change = 0
    for person in people:
        if person in seated:
            continue
        seated.append(person)
        change = search(seated, connections[(latest, person)])
        seated.pop()
        if last_person:
            change += connections[(seated[0], person)]
        if change > greatest_change:
            greatest_change = change
    return total_change + greatest_change

print(search(['Alice']))

for person in people:
    connections[('', person)] = 0
    connections[(person, '')] = 0
people.add('')
print(search(['Alice']))