package glistermelon;

import java.io.IOException;
import java.util.*;

public class Day10 extends DaySolver {

    public Day10() throws IOException {
        super(10);
    }

    char[][] map;
    List<R2> loop = null;

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
        loop = exploreLoop(start, null, start);
        assert loop != null;
        loop.addFirst(loop.removeLast());
        return String.valueOf((loop.size() + 1) / 2);
    }

    public String solvePart2() {
        List<R2> outside = fillSpace(new R2(0, 0), loop);
        Set<R2> inside = new HashSet<>();
        Dir in = null;
        R2 start = null;
        int i = 0, j = 1;
        while (!loop.get(i).equals(start)) {
            R2 p1 = loop.get(i);
            R2 p2 = loop.get(j);
            Dir dir = Dir.fromDelta(p2.sub(p1));
            if (in == null) {
                Dir d = dir.turnRight();
                if (outside.contains(d.advance(p1))) in = d.flip();
                else if (outside.contains(d.flip().advance(p1))) in = d;
                if (in != null) start = p1;
            }
            if (in != null) {
                R2 insidePoint = in.advance(p1);
                if (!loop.contains(insidePoint)) {
                    inside.addAll(fillSpace(insidePoint, loop));
                }
            }
            i++;
            j++;
            if (i == loop.size()) i = 0;
            if (j == loop.size()) j = 0;
            if (in != null) {
                Dir nextDir = Dir.fromDelta(loop.get(j).sub(loop.get(i)));
                if (dir.turnRight().equals(nextDir)) in = in.turnRight();
                else if (dir.turnLeft().equals(nextDir)) in = in.turnLeft();
            }
        };
        return String.valueOf(inside.size());
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
    private List<R2> exploreLoop(R2 start, R2 visited, R2 end) {
        if (visited == null) {
            for (Dir d : Dir.allDirections()) {
                if (canGo(start, d) && canGo(d.advance(start), d.flip())) {
                    visited = d.advance(start);
                    break;
                }
            }
        }
        List<R2> output = new ArrayList<>();
        while (true) {
            List<R2> candidates = new ArrayList<>();
            for (Dir d : Dir.allDirections()) {
                if (!canGo(start, d)) continue;
                R2 p = d.advance(start);
                if (p.equals(visited) || !canGo(p, d.flip())) continue;
                candidates.add(p);
            }
            switch (candidates.size()) {
                case 0:
                    return null;
                case 1:
                    R2 p = candidates.getFirst();
                    output.add(p);
                    if (p.equals(end)) return output;
                    visited = start;
                    start = p;
                    break;
                default:
                    for (R2 cp : candidates) {
                        List<R2> points = exploreLoop(cp, start, end);
                        if (points == null) continue;
                        output.addAll(points);
                        return output;
                    }
            }
        }
    }

    private List<R2> fillSpace(R2 start, List<R2> boundary) {
        List<R2> visited = new ArrayList<>();
        visited.add(start);
        Queue<R2> queue = new ArrayDeque<R2>();
        queue.add(start);
        while (!queue.isEmpty()) {
            for (R2 point : queue.poll().adjacent()) {
                if (point.getChar(map) == null || visited.contains(point) || boundary.contains(point)) continue;
                queue.add(point);
                visited.add(point);
            }
        }
        return visited;
    }

}
