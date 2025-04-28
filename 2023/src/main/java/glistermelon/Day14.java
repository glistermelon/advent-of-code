package glistermelon;

import org.apache.commons.math3.util.ArithmeticUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day14 extends DaySolver {

    public Day14() throws IOException {
        super(14);
    }

    enum TileType {
        None, Rock, Wall
    }

    TileType[][] map;
    final List<int[]> sequences = new ArrayList<>();
    List<List<Range>> seqGroups;

    public void runSharedLogic() {}

    private void loadMap() {

        map = Arrays.stream(this.puzzleInput.split("\n"))
                .map(s -> s.chars().mapToObj(
                        c -> switch (c) {
                            case '#' -> TileType.Wall;
                            case 'O' -> TileType.Rock;
                            default -> TileType.None;
                        }
                ).toArray(TileType[]::new)).toArray(TileType[][]::new);

        seqGroups = new ArrayList<>();
        for (TileType[] row : map) {
            List<Range> groups = new ArrayList<>();
            int x0 = 0, x1 = 0;
            while (x1 < row.length) {
                x0 = x1;
                while (x0 < row.length && row[x0] == TileType.Wall) x0++;
                if (x0 == row.length) break;
                x1 = x0;
                while (x1 < row.length && row[x1] != TileType.Wall) x1++;
                groups.add(new Range(x0, x1));
            }
            seqGroups.add(groups);
        }

    }

    public String solvePart1() {

        loadMap();

        doQuarterCycle(Dir.Up);
        return String.valueOf(getTotalLoad());
    }

    public String solvePart2() {

        loadMap();

        int start = 0, period = 0;

        while (true) {

            int[] seq = getSequence();

            for (int i = 0; i < sequences.size(); i++) {
                if (Arrays.equals(seq, sequences.get(i))) {
                    start = i;
                    period = sequences.size() - start;
                    break;
                }
            }
            if (period != 0) break;

            sequences.add(seq);
            doFullCycle();

        }

        int rem = (1000000000 - start) % period;
        for (int i = 0; i < rem; i++) doFullCycle();
        return String.valueOf(getTotalLoad());

    }

    private void doQuarterCycle(Dir gravity) {

        R2 corner;
        if (gravity.equals(Dir.Up)) corner = new R2(0, 0);
        else if (gravity.equals(Dir.Right)) corner = new R2(map[0].length - 1, 0);
        else if (gravity.equals(Dir.Down)) corner = new R2(map[0].length - 1, map.length - 1);
        else corner = new R2(0, map.length - 1);

        Dir perpendicular = gravity.turnRight();
        Dir antigravity = gravity.flip();
        for (R2 start = corner; inBounds(start); start = perpendicular.advance(start)) {
            R2 src = start, dst = start;
            do {
                TileType tile = map[src.y()][src.x()];
                if (tile == TileType.Wall) {
                    do {
                        src = antigravity.advance(src);
                    } while (inBounds(src) && map[src.y()][src.x()] == TileType.Wall);
                    dst = src;
                    continue;
                }
                else if (tile == TileType.Rock) {
                    map[src.y()][src.x()] = TileType.None;
                    map[dst.y()][dst.x()] = TileType.Rock;
                    dst = antigravity.advance(dst);
                }
                src = antigravity.advance(src);
            } while (inBounds(src));
        }

    }

    private void doFullCycle() {
        doQuarterCycle(Dir.Up);
        doQuarterCycle(Dir.Left);
        doQuarterCycle(Dir.Down);
        doQuarterCycle(Dir.Right);
    }

    private boolean inBounds(R2 p) {
        return p.x() >= 0 && p.y() >= 0 && p.x() < map[0].length && p.y() < map.length;
    }

    private int getTotalLoad() {

        int load = 0;
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] == TileType.Rock) load += map.length - y;
            }
        }
        return load;

    }

    private int[] getSequence() {

        List<Integer> counts = new ArrayList<>();
        for (int y = 0; y < map.length; y++) {
            for (Range range : seqGroups.get(y)) {
                int count = 0;
                for (long x = range.start(); x < range.end(); x++) {
                    if (map[y][(int)x] == TileType.Rock) count++;
                }
                counts.add(count);
            }
        }
        return counts.stream().mapToInt(Integer::intValue).toArray();

    }

}