package glistermelon;

import java.io.IOException;
import java.util.*;

public class Day18 extends DaySolver {

    public Day18() throws IOException {
        super(18);
    }

    public void runSharedLogic() {}

    public String solvePart1() {

        final List<R2> loop = new ArrayList<>();
        int width = Integer.MIN_VALUE, height = Integer.MIN_VALUE;
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        R2 pos = new R2(0, 0);

        for (String line : getPuzzleInputLines()) {

            String[] split = line.split(" ");
            Dir dir = Dir.fromChar(split[0].charAt(0));
            assert dir != null;
            int steps = Integer.parseInt(split[1]);

            for (int i = 0; i < steps; i++) {
                loop.add(pos);
                pos = dir.advance(pos);
                if (pos.x() > width) width = pos.x();
                else if (pos.x() < minX) minX = pos.x();
                if (pos.y() > height) height = pos.y();
                else if (pos.y() < minY) minY = pos.y();
            }

        }

        final int minXf = minX;
        final int minYf = minY;
        loop.replaceAll(p -> new R2(p.x() - minXf + 1, p.y() - minYf + 1));
        width = width - minX + 3;
        height = height - minY + 3;

        int area = width * height - fillSpace(new R2(0, 0), loop, width, height).size();

        return String.valueOf(area);

    }

    public String solvePart2() {

        List<Wall> walls = new ArrayList<>();
        List<Bar> bars = new ArrayList<>();
        List<Long> barHeights = new ArrayList<>();

        long minY = Long.MAX_VALUE, maxY = Long.MIN_VALUE;

        R2L pos = new R2L(0, 0);
        for (String line : getPuzzleInputLines()) {

            String hex = line.split(" ")[2].substring(2);
            Dir dir = switch (hex.charAt(5)) {
                case '0' -> Dir.Right;
                case '1' -> Dir.Down;
                case '2' -> Dir.Left;
                case '3' -> Dir.Up;
                default -> null;
            };
            assert dir != null;
            long steps = Long.parseLong(hex.substring(0, 5), 16);

            if (dir.equals(Dir.Up) || dir.equals(Dir.Down)) {
                long y1 = pos.y();
                pos = dir.advance(pos, steps);
                long y2 = pos.y();
                if (y2 < y1) {
                    var temp = y1;
                    y1 = y2;
                    y2 = temp;
                }
                y2++; // exclusive upper bound
                walls.add(new Wall(new Range(y1, y2), pos.x()));
                if (y1 < minY) minY = y1;
                if (y2 > maxY) maxY = y2;
            }
            else {
                long x1 = pos.x();
                pos = dir.advance(pos, steps);
                long x2 = pos.x();
                if (x2 < x1) {
                    var temp = x1;
                    x1 = x2;
                    x2 = temp;
                }
                x2++; // exclusive upper bound
                bars.add(new Bar(new Range(x1, x2), pos.y()));
                if (!barHeights.contains(pos.y())) barHeights.add(pos.y());
                if (!barHeights.contains(1 + pos.y())) barHeights.add(1 + pos.y());
            }

        }

        barHeights.sort(Long::compare);

        long totalArea = 0;

        for (int barYIdx = 0; barYIdx < barHeights.size(); barYIdx++) {

            long y = barHeights.get(barYIdx);

            long area = 0;
            long mult = 1;
            if (barYIdx + 1 != barHeights.size()) mult = barHeights.get(barYIdx + 1) - y;

            List<Wall> switches = new ArrayList<>();
            for (Wall wall : walls) {
                if (wall.range().contains(y)) switches.add(wall);
            }
            switches.sort(Comparator.comparingLong(Wall::x));

            boolean inside = true;
            for (int i = 0; i < switches.size() - 1;) {

                Wall w1 = switches.get(i), w2 = switches.get(i + 1);
                if (bars.contains(new Bar(new Range(w1.x(), w2.x() + 1), y))) {
                    if (pointInLoop(walls, bars, new R2L(w2.x() + 1, y))) i++;
                    else i += 2;
                    inside = true;
                }
                else if (inside) {
                    area += w2.x() - w1.x() - 1;
                    i++;
                    inside = false;
                }
                else {
                    i++;
                    inside = true;
                }

            }

            totalArea += area * mult;

        }

        long perimeter = 0;
        Set<R2L> corners = new HashSet<>();
        for (Wall w : walls) {
            perimeter += w.range().size() - 2;
            corners.add(new R2L(w.x(), w.range().start()));
            corners.add(new R2L(w.x(), w.range().end() - 1));
        }
        for (Bar b : bars) {
            perimeter += b.range().size() - 2;
            corners.add(new R2L(b.range().start(), b.y()));
            corners.add(new R2L(b.range().end() - 1, b.y()));
        }
        perimeter += corners.size();
        totalArea += perimeter;

        return String.valueOf(totalArea);

    }

    private boolean pointInLoop(List<Wall> walls, List<Bar> bars, R2L p) {

        int parity = 0;

        Map<Wall, R2L> checkWalls = new HashMap<>();
        Map<Bar, R2L> checkBars = new HashMap<>();

        for (Wall w : walls) {
            if (w.x() < p.x()) continue;
            long y = w.x() + p.y() - p.x();
            if (y < p.y()) continue;
            if (w.range().contains(y)) {
                if (w.range().isInclusiveEndpoint(y)) checkWalls.put(w, new R2L(w.x(), y));
                else parity++;
            }
        }

        for (Bar b : bars) {
            if (b.y() < p.y()) continue;
            long x = b.y() + p.x() - p.y();
            if (x < p.x()) continue;
            if (b.range().contains(x)) {
                if (b.range().isInclusiveEndpoint(x)) checkBars.put(b, new R2L(x, b.y()));
                else parity++;
            }
        }

        int corners = 0;
        for (Wall w : checkWalls.keySet()) {
            for (Bar b : checkBars.keySet()) {
                if (!checkWalls.get(w).equals(checkBars.get(b))) continue;
                R2L ip = checkWalls.get(w);
                if (
                        (w.range().start() == ip.y() && b.range().start() == ip.x())
                        || (w.range().end() - 1 == ip.y() && b.range().end() - 1 == ip.x())
                ) corners++;
            }
        }

        parity += checkWalls.size() + checkBars.size() + corners;

        return parity % 2 != 0;

    }

    private record Wall(Range range, long x) {}
    private record Bar(Range range, long y) {}

    private List<R2> fillSpace(R2 start, List<R2> boundaryList, int width, int height) {

        Set<R2> boundary = new HashSet<>(boundaryList.size());
        boundary.addAll(boundaryList);
        boolean[][] visited = new boolean[width][height];
        visited[start.x()][start.y()] = true;
        Queue<R2> queue = new ArrayDeque<>();
        queue.add(start);
        List<R2> output = new ArrayList<>();
        output.add(start);

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
