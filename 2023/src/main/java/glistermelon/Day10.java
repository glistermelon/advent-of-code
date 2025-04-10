package glistermelon;

import java.io.IOException;
import java.util.*;

public class Day10 extends DaySolver {

    public Day10() throws IOException {
        super(10);
    }

    char[][] map;
    List<R2> loop = null;

    public void runSharedLogic() {}

    public String solvePart1() {
        map = Arrays.stream(this.puzzleInput.split("\n"))
                .map(String::toCharArray).toArray(char[][]::new);
        R2 start = R2.find(map, 'S');
        assert start != null;
        for (Dir d1 : Dir.allDirections()) {
            for (int d2i = d1.num() + 1; d2i < 4; d2i++) {
                Dir d2 = new Dir(d2i);
                if (d1.equals(d2)) continue;
                R2 p1 = d1.advance(start);
                R2 p2 = d2.advance(start);
                if (canGo(p1, d1.flip()) && canGo(p2, d2.flip())) {
                    char c = '\0';
                    if (d1.equals(Dir.Up)) {
                        if (d2.equals(Dir.Right)) c = 'L';
                        else if (d2.equals(Dir.Down)) c = '|';
                        else if (d2.equals(Dir.Left)) c = 'J';
                    } else if (d1.equals(Dir.Right)) {
                        if (d2.equals(Dir.Down)) c = 'F';
                        else if (d2.equals(Dir.Left)) c = '-';
                    } else if (d1.equals(Dir.Down)) c = '7';
                    map[start.y()][start.x()] = c;
                }
            }
        }
        loop = exploreLoop(start);
        return String.valueOf((loop.size() + 1) / 2);
    }

    public String solvePart2() {

        List<R2> barriers = new ArrayList<>();
        for (R2 p : loop) {
            int x = p.x(), y = p.y();
            char c = map[y][x];
            x = 3 * x + 1;
            y = 3 * y + 1;
            barriers.add(new R2(x, y));
            switch (c) {
                case '|':
                    barriers.add(new R2(x, y - 1));
                    barriers.add(new R2(x, y + 1));
                    break;
                case '-':
                    barriers.add(new R2(x - 1, y));
                    barriers.add(new R2(x + 1, y));
                    break;
                case 'F':
                    barriers.add(new R2(x + 1, y));
                    barriers.add(new R2(x, y + 1));
                    break;
                case '7':
                    barriers.add(new R2(x - 1, y));
                    barriers.add(new R2(x, y + 1));
                    break;
                case 'J':
                    barriers.add(new R2(x, y - 1));
                    barriers.add(new R2(x - 1, y));
                    break;
                case 'L':
                    barriers.add(new R2(x, y - 1));
                    barriers.add(new R2(x + 1, y));
                    break;
                default:
                    break;
            }
        }
        List<R2> outside = fillSpace(new R2(0, 0), barriers, 3 * map[0].length, 3 * map.length);
        int result = 0;
        for (int y = 0; y < 3 * map.length; y += 3) {
            for (int x = 0; x < 3 * map[0].length; x += 3) {
                boolean in = true;
                for (int dx = 0; dx < 3; dx++) {
                    for (int dy = 0; dy < 3; dy++) {
                        R2 p = new R2(x + dx, y + dy);
                        if (outside.contains(p) || barriers.contains(p)) {
                            in = false;
                            break;
                        }
                    }
                    if (!in) break;
                }
                if (in) result++;
            }
        }

        return String.valueOf(result);

    }

    private boolean canGo(R2 p, Dir d) {
        Character boxedC = p.getChar(map);
        if (boxedC == null) return false;
        char c = boxedC;
        return switch (d.num()) {
            case 0 -> c == '|' || c == 'L' || c == 'J';
            case 1 -> c == '-' || c == 'L' || c == 'F';
            case 2 -> c == '|' || c == '7' || c == 'F';
            case 3 -> c == '-' || c == '7' || c == 'J';
            default -> throw new IllegalStateException("Unexpected value: " + d.num());
        };
    }

    // map must be initialized and S must be identified
    private List<R2> exploreLoop(R2 start) {
        List<R2> points = new ArrayList<>();
        points.add(start);
        R2 prev = null;
        do {
            for (Dir d : Dir.allDirections()) {
                R2 next = d.advance(start);
                if (!next.equals(prev) && canGo(start, d) && canGo(next, d.flip())) {
                    prev = start;
                    start = next;
                    points.add(start);
                    break;
                }
            }
        } while (!points.getFirst().equals(start));
        points.removeLast();
        return points;
    }

    private List<R2> fillSpace(R2 start, List<R2> boundaryList, int width, int height) {
        Set<R2> boundary = new HashSet<>(boundaryList.size());
        boundary.addAll(boundaryList);
        boolean[][] visited = new boolean[width][height];
        visited[start.x()][start.y()] = true;
        Queue<R2> queue = new ArrayDeque<>();
        queue.add(start);
        List<R2> output = new ArrayList<>();
        while (!queue.isEmpty()) {
            for (R2 point : queue.poll().adjacent()) {
                int x = point.x(), y = point.y();
                if (
                        x < 0 || y < 0
                        || x >= width || y >= height
                        || visited[x][y] || boundary.contains(point)
                ) continue;
                queue.add(point);
                visited[x][y] = true;
                output.add(point);
            }
        }
        return output;
    }

}
