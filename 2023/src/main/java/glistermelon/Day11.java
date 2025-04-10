package glistermelon;

import org.apache.commons.math3.util.Combinations;

import java.io.IOException;
import java.util.*;

public class Day11 extends DaySolver {

    public Day11() throws IOException {
        super(11);
    }

    public void runSharedLogic() {}

    public String solve(int expansion) {

        List<R2L> galaxies = new ArrayList<>();
        char[][] map = Arrays.stream(getPuzzleInputLines()).map(String::toCharArray).toArray(char[][]::new);
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] == '#') galaxies.add(new R2L(x, y));
            }
        }

        Map<R2L, Integer> dy = new HashMap<>();
        for (int y = 0; y < map.length; y++) {
            int finalY = y;
            if (galaxies.stream().noneMatch(g -> g.y() == finalY)) {
                for (R2L g : galaxies) {
                    if (g.y() > y) dy.put(g, dy.getOrDefault(g, 0) + expansion - 1);
                }
            }
        }

        Map<R2L, Integer> dx = new HashMap<>();
        for (int x = 0; x < map[0].length; x++) {
            int finalX = x;
            if (galaxies.stream().noneMatch(g -> g.x() == finalX)) {
                for (R2L g : galaxies) {
                    if (g.x() > x) dx.put(g, dx.getOrDefault(g, 0) + expansion - 1);
                }
            }
        }

        galaxies.replaceAll(g -> new R2L(
                g.x() + dx.getOrDefault(g, 0),
                g.y() + dy.getOrDefault(g, 0)
        ));

        long sum = 0;
        for (int[] combo : new Combinations(galaxies.size(), 2)) {
            var g1 = galaxies.get(combo[0]);
            var g2 = galaxies.get(combo[1]);
            sum += g1.taxiDist(g2);
        }
        return String.valueOf(sum);

    }

    public String solvePart1() {
        return solve(2);
    }

    public String solvePart2() {
        return solve(1000000);
    }

}