package glistermelon;

import java.io.IOException;
import java.util.*;

public class Day22 extends DaySolver {

    public Day22() throws IOException {
        super(22);
    }

    List<Brick> bricks = new ArrayList<>();
    Map<Brick, List<Brick>> criticalSupports = new HashMap<>();
    Map<Set<Brick>, List<Brick>> supports = new HashMap<>();

    public void runSharedLogic() {

        for (String line : getPuzzleInputLines()) {

            int[] nums = Arrays.stream(line.replace("~", ",").split(","))
                    .mapToInt(Integer::parseInt).toArray();

            Integer[] boxedNums = new Integer[3];
            Range range = null;
            Brick.CoordAxis axis = Brick.CoordAxis.X;

            for (int i = 0; i < 3; i++) {
                final int n1 = nums[i];
                final int n2 = nums[3 + i];
                if (n1 == n2) boxedNums[i] = n1;
                else {
                    range = new Range(Integer.min(n1, n2), Integer.max(n1, n2) + 1);
                    if (i == 1) axis = Brick.CoordAxis.Y;
                    else if (i == 2) axis = Brick.CoordAxis.Z;
                }
            }
            if (range == null) {
                axis = Brick.CoordAxis.Z;
                range = new Range(nums[2], nums[2] + 1);
            }

            bricks.add(new Brick(boxedNums[0], boxedNums[1], boxedNums[2], axis, range));

        }

        List<Brick> falling = new ArrayList<>(bricks);
        falling.sort((a, b) -> {
            int z1 = a.rangedAxis == Brick.CoordAxis.Z ? (int)a.range.start() : a.z;
            int z2 = b.rangedAxis == Brick.CoordAxis.Z ? (int)b.range.start() : b.z;
            return z1 - z2;
        });

        for (int index = 0; index < falling.size(); index++) {
            Brick brick = falling.get(index);
            int fallTo = 1;
            List<Brick> atop = new ArrayList<>();
            for (int i = 0; i < index; i++) {
                Brick other = falling.get(i);
                if (!brick.overlaps(other)) continue;
                int z = other.maxZ() + 1;
                if (z > fallTo) {
                    fallTo = z;
                    atop.clear();
                    atop.add(other);
                }
                else if (z == fallTo) atop.add(other);
            }
            if (atop.size() == 1) {
                Brick other = atop.getFirst();
                List<Brick> necessary = criticalSupports.getOrDefault(other, new ArrayList<>());
                necessary.add(brick);
                criticalSupports.put(other, necessary);
            }
            if (!atop.isEmpty()) {
                Set<Brick> others = new HashSet<>(atop);
                List<Brick> necessary = supports.containsKey(others) ? supports.remove(others) : new ArrayList<>();
                necessary.add(brick);
                supports.put(others, necessary);
            }
            brick.move(0, 0, fallTo - brick.minZ());
        }

    }

    public String solvePart1() {

        return String.valueOf(bricks.size() - criticalSupports.size());

    }

    public String solvePart2() {

        int sum = 0;

        for (Brick base : criticalSupports.keySet()) {
            Set<Brick> fallen = new HashSet<>();
            Set<Set<Brick>> visited = new HashSet<>();
            Set<Brick> buffer = new HashSet<>();
            buffer.add(base);
            while (!buffer.isEmpty()) {
                fallen.addAll(buffer);
                Set<Brick> next = new HashSet<>();
                for (Set<Brick> key : supports.keySet()) {
                    if (fallen.containsAll(key) && !visited.contains(key)) {
                        visited.add(key);
                        next.addAll(supports.get(key));
                    }
                }
                buffer = next;
            }
            sum += fallen.size() - 1;
        }

        return String.valueOf(sum);
    }

    private static class Brick {

        enum CoordAxis {
            X, Y, Z
        }

        CoordAxis rangedAxis;
        Range range;
        int x, y, z;

        public Brick(Integer x, Integer y, Integer z, CoordAxis rangedAxis, Range range) {
            this.x = x == null ? 0 : x;
            this.y = y == null ? 0 : y;
            this.z = z == null ? 0 : z;
            this.rangedAxis = rangedAxis;
            this.range = range;
        }

        public boolean overlaps(Brick other) {

            if (rangedAxis == CoordAxis.Z)
                return other.containsPoint(x, y);
            if (other.rangedAxis == CoordAxis.Z)
                return this.containsPoint(other.x, other.y);

            if (rangedAxis == CoordAxis.X && other.rangedAxis == CoordAxis.X)
                return y == other.y && range.overlaps(other.range);

            if (rangedAxis == CoordAxis.Y && other.rangedAxis == CoordAxis.Y)
                return x == other.x && range.overlaps(other.range);

            if (rangedAxis == CoordAxis.X)
                return range.contains(other.x) && other.range.contains(y);
            else
                return range.contains(other.y) && other.range.contains(x);

        }

        public boolean containsPoint(int x, int y) {
            return switch (rangedAxis) {
                case CoordAxis.X -> range.contains(x) && this.y == y;
                case CoordAxis.Y -> range.contains(y) && this.x == x;
                case CoordAxis.Z -> this.x == x && this.y == y;
            };
        }

        public String toString() {
            return "(" + x + ", " + y + ", " + z + ")";
        }

        public int maxZ() {
            return rangedAxis == CoordAxis.Z ? (int)range.end() - 1 : z;
        }

        public int minZ() {
            return rangedAxis == CoordAxis.Z ? (int)range.start() : z;
        }

        public void move(int x, int y, int z) {
            switch (rangedAxis) {
                case CoordAxis.X:
                    range = new Range(range.start() + x, range.end() + x);
                    this.y += y;
                    this.z += z;
                    break;
                case CoordAxis.Y:
                    range = new Range(range.start() + y, range.end() + y);
                    this.x += x;
                    this.z += z;
                    break;
                case CoordAxis.Z:
                    range = new Range(range.start() + z, range.end() + z);
                    this.x += x;
                    this.y += y;
                    break;
            };
        }

    }

}