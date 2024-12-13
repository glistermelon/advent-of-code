city_distances = {}
all_cities = set()

def get_distance(cities):
    if cities not in city_distances:
        return city_distances[tuple(reversed(cities))]
    return city_distances[cities]

for ln in open('../inputs/9.txt'):
    ln = ln.split()
    city1 = ln[0]
    city2 = ln[2]
    dist = int(ln[-1])
    city_distances[(city1, city2)] = dist
    all_cities.add(city1)
    all_cities.add(city2)

for part1 in True, False:

    result = sum(city_distances.values()) if part1 else 0

    def search(path, total_distance = 0):
        global result
        src_city = path[-1]
        for city in all_cities:
            if city in path:
                continue
            dist = get_distance((src_city, city))
            if part1 and total_distance + dist >= result:
                continue
            total_distance += dist
            path.append(city)
            if len(path) == len(all_cities):
                if part1 or total_distance > result:
                    result = total_distance
            else:
                search(path, total_distance)
            total_distance -= dist
            path.pop()

    for city in all_cities:
        search([city])
    print(result)
