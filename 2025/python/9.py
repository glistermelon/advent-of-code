from itertools import combinations
from dataclasses import dataclass
import shapely

puzzle_input = open('../inputs/9.txt').read()

points = None

@dataclass(frozen=True)
class Point:
    x: int
    y: int

def process_input():
    global points
    points = [Point(*map(int, ln.split(','))) for ln in puzzle_input.splitlines(keepends=False)]

def area_between(p1, p2):
    return (abs(p1.x - p2.x) + 1) * (abs(p1.y - p2.y) + 1)

def solve_part_1():
    return area_between(*max(combinations(points, 2), key=lambda c:area_between(*c)))

# This shit works but its slow as hell and I dont want to optimize it
# I tried shapely initially and it didn't work but it ended up being a mistake in area_between

# from enum import Enum

# class Dir(Enum):
#     Up = 0
#     Right = 1
#     Down = 2
#     Left = 3

# def segment_direction(p1: Point, p2: Point) -> Dir:
#     if p1 == p2:
#         return None
#     if p1.x == p2.x:
#         if p2.y > p1.y:
#             return Dir.Down
#         else:
#             return Dir.Up
#     elif p1.y == p2.y:
#         if p2.x > p1.x:
#             return Dir.Right
#         else:
#             return Dir.Left
#     else:
#         return None

# class Orientation(Enum):
#     Clockwise = -1
#     Counterclockwise = 1

# def segments_around(points) -> Generator[tuple[Point, Point], None, None]:
#     for i in range(len(points) - 1):
#         yield (points[i], points[i + 1])
#     yield (points[-1], points[0])

# def determine_orientation() -> Orientation:
#     ccw_turns = 0
#     segment_gen = segments_around(points)
#     first_segment = next(segment_gen)
#     dir = segment_direction(*first_segment)
#     for s in chain(segment_gen, (first_segment,)):
#         new_dir = segment_direction(*s)
#         ccw_turns += 1 if (new_dir.value - dir.value) % 4 == 3 else -1
#         dir = new_dir
#     return Orientation.Counterclockwise if ccw_turns > 0 else Orientation.Clockwise

# def solve_part_2():

#     if determine_orientation() != Orientation.Clockwise:
#         print('script expects clockwise orientation')
#         print('you can probably just reverse the point list')
#         return None

#     vertical_divides = set()
#     horizontal_divides = set()
#     for p1, p2 in segments_around(points):
#         dir = segment_direction(p1, p2)
#         if dir == Dir.Up:
#             vertical_divides.add(p1.x)
#         elif dir == Dir.Down:
#             vertical_divides.add(p1.x + 1)
#         elif dir == Dir.Right:
#             horizontal_divides.add(p1.y)
#         elif dir == Dir.Left:
#             horizontal_divides.add(p1.y + 1)
#         else:
#             print('invalid direction')
#             return None
    
#     section_map_w = len(vertical_divides) - 1
#     section_map_h = len(horizontal_divides) - 1
#     section_map = [[False] * section_map_h for _ in range(section_map_w)]

#     vertical_divides = sorted(vertical_divides)
#     horizontal_divides = sorted(horizontal_divides)

#     point_to_section = {
#         p: (
#             next(i for i in range(len(vertical_divides) - 1) if p.x >= vertical_divides[i] and p.x < vertical_divides[i + 1]),
#             next(i for i in range(len(horizontal_divides) - 1) if p.y >= horizontal_divides[i] and p.y < horizontal_divides[i + 1])
#         )
#         for p in points
#     }

#     section_loop = []
#     for p1, p2 in segments_around(points):
#         for x in range(min(p1.x, p2.x), max(p1.x, p2.x) + 1):
#             for y in range(min(p1.y, p2.y), max(p1.y, p2.y) + 1):
#                 seg_x = next(i for i in range(len(vertical_divides) - 1) if x >= vertical_divides[i] and x < vertical_divides[i + 1])
#                 seg_y = next(i for i in range(len(horizontal_divides) - 1) if y >= horizontal_divides[i] and y < horizontal_divides[i + 1])
#                 if not section_map[seg_x][seg_y]:
#                     section_map[seg_x][seg_y] = True
#                     section_loop.append(Point(seg_x, seg_y))

#     # This approach probably wouldn't work for any problem like this
#     # but it works for this example input because there's no funny business

#     def flood_fill(root):
#         queue = { root }
#         points = { root }
#         while queue:
#             seg_x, seg_y = queue.pop()
#             for dx, dy in ((1, 0), (-1, 0), (0, 1), (0, -1)):
#                 ax, ay = seg_x + dx, seg_y + dy
#                 if ax < -1 or ay < -1 or ax > len(section_map) or ay > len(section_map[0]):
#                     continue
#                 if ax >= 0 and ax < len(section_map) and ay >= 0 and ay < len(section_map[0]) and section_map[ax][ay]:
#                     continue
#                 if (ax, ay) not in points:
#                     queue.add((ax, ay))
#                     points.add((ax, ay))
#         return [(x, y) for x, y in points if x >= 0 and y >= 0 and x < len(section_map) and y < len(section_map[0])]

#     outside = flood_fill((0, 0))
#     section_map = [[True] * section_map_h for _ in range(section_map_w)]
#     for x, y in outside:
#         section_map[x][y] = False

#     max_area = 0
#     for p1, p2 in combinations(points, 2):
#         p1_sx, p1_sy = point_to_section[p1]
#         p2_sx, p2_sy = point_to_section[p2]
#         min_sx, max_sx = min(p1_sx, p2_sx), max(p1_sx, p2_sx)
#         min_sy, max_sy = min(p1_sy, p2_sy), max(p1_sy, p2_sy)
#         if all(section_map[sx][sy] for sx in range(min_sx, max_sx + 1) for sy in range(min_sy, max_sy + 1)):
#             area = area_between(p1, p2)
#             if area > max_area:
#                 max_area = area

#     return max_area

def solve_part_2():
    poly = shapely.Polygon([shapely.Point(p.x, p.y) for p in points])
    max_area = 0
    for p1, p2 in combinations(points, 2):
        min_x, max_x = min(p1.x, p2.x), max(p1.x, p2.x)
        min_y, max_y = min(p1.y, p2.y), max(p1.y, p2.y)
        rect = shapely.Polygon([(min_x, min_y), (max_x, min_y), (max_x, max_y), (min_x, max_y)])
        if poly.covers(rect):
            area = area_between(p1, p2)
            if area > max_area:
                max_area = area
    return max_area

process_input()
print(solve_part_1())
print(solve_part_2())