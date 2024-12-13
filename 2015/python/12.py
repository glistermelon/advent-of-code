import json

part1 = True

def process(data):
    if type(data) is int:
        return data
    if type(data) is list:
        return sum(process(item) for item in data)
    if type(data) is dict and (part1 or 'red' not in data.values()):
        return sum(process(item) for item in data.values())
    return 0

data = json.loads(open('../inputs/12.txt').read())
print(process(data))
part1 = False
print(process(data))