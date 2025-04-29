package glistermelon;

import java.io.IOException;
import java.util.*;

// copyable template

public class Day21 extends DaySolver {

    public Day21() throws IOException {
        super(21);
    }

    char[][] map;
    R2 start = null;

    public void runSharedLogic() {

        map = Arrays.stream(this.puzzleInput.split("\n"))
                .map(String::toCharArray).toArray(char[][]::new);

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] == 'S') {
                    start = new R2(x, y);
                    break;
                }
            }
            if (start != null) break;
        }

    }

    public String solvePart1() {

        Set<R2> buffer = new HashSet<>();
        buffer.add(start);

        for (int i = 0; i < 64; i++) {
            Set<R2> next = new HashSet<>();
            for (R2 p : buffer) {
                for (R2 a : p.adjacent()) {
                    if (inBounds(a) && map[a.y()][a.x()] != '#') next.add(a);
                }
            }
            buffer = next;
        }

        return String.valueOf(buffer.size());

    }

    public String solvePart2() {

        return "";

    }

    private boolean inBounds(R2 p) {
        return p.x() >= 0 && p.y() >= 0 && p.x() < map[0].length && p.y() < map.length;
    }

}