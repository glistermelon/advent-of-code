package glistermelon;

import java.io.IOException;
import java.util.*;

// copyable template

public class Day21 extends DaySolver {

    public Day21() throws IOException {
        super(21);
    }

    char[][] map;

    public void runSharedLogic() {

        map = Arrays.stream(this.puzzleInput.split("\n"))
                .map(String::toCharArray).toArray(char[][]::new);

    }

    public String solvePart1() {

        Set<R2> buffer = new HashSet<>();
        buffer.add(getStartPos());

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

        final int totalSteps = 26501365;

        // for even totalSteps use evenMax instead
        long plots = oddMax(scoutMap(getStartPos()));

        for (int rotIdx = 0; rotIdx < 4; rotIdx++) {

            final Map<Integer, Integer> axisTileData = scoutMap(new R2(0, getStartPos().y()));
            final Map<Integer, Integer> normTileData = scoutMap(new R2(0, 0));
            final int axisMaxValEven = evenMax(axisTileData);
            final int axisMaxValOdd = oddMax(axisTileData);
            final int normMaxValEven = evenMax(normTileData);
            final int normMaxValOdd = oddMax(normTileData);
            final int axisMaxKey = axisTileData.keySet().stream().max(Integer::compare).get();
            final int normMaxKey = normTileData.keySet().stream().max(Integer::compare).get();
            final int mapSize = map.length;
            int baseSteps;
            int basicTiles;

            baseSteps = totalSteps - (mapSize + 1) / 2;
            basicTiles = (baseSteps - axisMaxKey) / mapSize + 1;
            plots += (long)(basicTiles + 1) / 2 * (baseSteps % 2 == 0 ? axisMaxValEven : axisMaxValOdd)
                    + (long)basicTiles / 2 * (baseSteps % 2 == 1 ? axisMaxValEven : axisMaxValOdd);
            for (int steps = baseSteps - mapSize * basicTiles; steps >= 0; steps -= mapSize)
                plots += axisTileData.get(steps);

            baseSteps -= (mapSize + 1) / 2;
            for (int n = 0; n < (baseSteps + mapSize) / mapSize; n++) {
                basicTiles = Integer.max((baseSteps - mapSize * n - normMaxKey + mapSize) / mapSize, 0);
                if (n % 2 == 0) {
                    plots += (long)(basicTiles + 1) / 2 * (baseSteps % 2 == 0 ? normMaxValEven : normMaxValOdd)
                            + (long)basicTiles / 2 * (baseSteps % 2 == 1 ? normMaxValEven : normMaxValOdd);
                }
                else {
                    plots += (long)(basicTiles + 1) / 2 * (baseSteps % 2 == 0 ? normMaxValOdd : normMaxValEven)
                            + (long)basicTiles / 2 * (baseSteps % 2 == 1 ? normMaxValOdd : normMaxValEven);
                }
                for (int steps = baseSteps - mapSize * n - mapSize * basicTiles; steps >= 0; steps -= mapSize) {
                    plots += normTileData.get(steps);
                }

            }

            rotateMap();

        }

        return String.valueOf(plots);

    }

    private boolean inBounds(R2 p) {
        return p.x() >= 0 && p.y() >= 0 && p.x() < map[0].length && p.y() < map.length;
    }

    private R2 getStartPos() {
        return new R2(map[0].length / 2, map.length / 2);
    }

    private void rotateMap() {

        R2 center = getStartPos();
        int cx = center.x(), cy = center.y();

        char[][] rot = new char[map[0].length][map.length];

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                rot[cy - (x - cx)][cx + (y - cy)] = map[y][x];
            }
        }

        map = rot;

    }

    private Map<Integer, Integer> scoutMap(R2 startPos) {

        Set<R2> buffer = new HashSet<>();
        buffer.add(startPos);

        Map<Integer, Integer> output = new HashMap<>();
        output.put(0, 1);

        int steps = 0, prevSize = 0;
        while (steps % 2 == 1 || buffer.size() != prevSize) {
            if (steps % 2 == 0) prevSize = buffer.size();
            Set<R2> next = new HashSet<>();
            for (R2 p : buffer) {
                for (R2 a : p.adjacent()) {
                    if (inBounds(a) && map[a.y()][a.x()] != '#') next.add(a);
                }
            }
            buffer = next;
            output.put(++steps, buffer.size());
        }

        return output;

    }

    private int evenMax(Map<Integer, Integer> data) {
        return data.get(
                data.keySet().stream().filter(k -> k % 2 == 0).max(Integer::compareTo).get()
        );
    }

    private int oddMax(Map<Integer, Integer> data) {
        return data.get(
                data.keySet().stream().filter(k -> k % 2 == 1).max(Integer::compareTo).get()
        );
    }

}