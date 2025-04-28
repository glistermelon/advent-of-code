package glistermelon;

import java.io.IOException;
import java.util.*;

// copyable template

public class Day16 extends DaySolver {

    public Day16() throws IOException {
        super(16);
    }

    char[][] map;

    public void runSharedLogic() {

        map = Arrays.stream(this.puzzleInput.split("\n"))
                .map(String::toCharArray).toArray(char[][]::new);

    }

    public String solvePart1() {

        return String.valueOf(getEnergizedTiles(new R2(0, 0), Dir.Right));

    }

    public String solvePart2() {

        int max = 0;

        for (R2 start = new R2(0, 0); start.x() < map[0].length; start = Dir.Right.advance(start))
            max = Integer.max(max, getEnergizedTiles(start, Dir.Down));
        for (R2 start = new R2(0, 0); start.y() < map.length; start = Dir.Down.advance(start))
            max = Integer.max(max, getEnergizedTiles(start, Dir.Right));
        for (R2 start = new R2(map[0].length - 1, map.length - 1); start.x() >= 0; start = Dir.Left.advance(start))
            max = Integer.max(max, getEnergizedTiles(start, Dir.Up));
        for (R2 start = new R2(map[0].length - 1, map.length - 1); start.y() >= 0; start = Dir.Up.advance(start))
            max = Integer.max(max, getEnergizedTiles(start, Dir.Left));

        return String.valueOf(max);
    }

    private record Beam(R2 pos, Dir dir) {

        public char charAt(char[][] map) {
            return map[pos.y()][pos.x()];
        }

        public boolean inBounds(char[][] map) {
            return pos.x() >= 0 && pos.y() >= 0 && pos.x() < map[0].length && pos.y() < map.length;
        }

    }

    private int getEnergizedTiles(R2 startPos, Dir startDir) {

        List<Beam> beams = new ArrayList<>();
        Set<R2> energized = new HashSet<>();
        Set<Beam> history = new HashSet<>();

        beams.add(new Beam(startPos, startDir));
        while (!beams.isEmpty()) {

            history.addAll(beams);

            Set<Beam> nextBeams = new HashSet<>();

            for (Beam beam : beams) {
                energized.add(beam.pos());
                R2 pos = beam.pos();
                Dir dir = beam.dir();
                switch (beam.charAt(map)) {
                    case '.':
                        nextBeams.add(new Beam(dir.advance(pos), dir));
                        break;
                    case '/':
                        if (dir.equals(Dir.Up) || dir.equals(Dir.Down)) dir = dir.turnRight();
                        else dir = dir.turnLeft();
                        nextBeams.add(new Beam(dir.advance(pos), dir));
                        break;
                    case '\\':
                        if (dir.equals(Dir.Up) || dir.equals(Dir.Down)) dir = dir.turnLeft();
                        else dir = dir.turnRight();
                        nextBeams.add(new Beam(dir.advance(pos), dir));
                        break;
                    case '-':
                        if (dir.equals(Dir.Right) || dir.equals(Dir.Left))
                            nextBeams.add(new Beam(dir.advance(pos), dir));
                        else {
                            Dir d1 = dir.turnLeft(), d2 = dir.turnRight();
                            nextBeams.add(new Beam(d1.advance(pos), d1));
                            nextBeams.add(new Beam(d2.advance(pos), d2));
                        }
                        break;
                    case '|':
                        if (dir.equals(Dir.Up) || dir.equals(Dir.Down))
                            nextBeams.add(new Beam(dir.advance(pos), dir));
                        else {
                            Dir d1 = dir.turnLeft(), d2 = dir.turnRight();
                            nextBeams.add(new Beam(d1.advance(pos), d1));
                            nextBeams.add(new Beam(d2.advance(pos), d2));
                        }
                        break;
                }
            }

            beams.clear();
            beams.addAll(nextBeams.stream().filter(b -> b.inBounds(map) && !history.contains(b)).toList());

        }

        return energized.size();

    }

}