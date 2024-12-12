def get_points(instructions):
    pos = [0, 0]
    points = set((tuple(pos),))
    for i in instructions:
        if i == '^': pos[1] += 1
        elif i == 'v': pos[1] -= 1
        elif i == '>': pos[0] += 1
        else: pos[0] -= 1
        points.add(tuple(pos))
    return points

instructions = open('../inputs/3.txt').read()
print(len(get_points(instructions)))
points = get_points(ins for idx, ins in enumerate(instructions) if idx % 2 == 0)
points.update(get_points(ins for idx, ins in enumerate(instructions) if idx % 2 == 1))
print(len(points))