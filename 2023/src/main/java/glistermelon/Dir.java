package glistermelon;

public record Dir(int num) {

    public static final Dir Up = new Dir(0);
    public static final Dir Right = new Dir(1);
    public static final Dir Down = new Dir(2);
    public static final Dir Left = new Dir(3);

    public static Dir[] allDirections() {
        Dir[] dirs = new Dir[4];
        for (int i = 0; i < 4; i++) dirs[i] = new Dir(i);
        return dirs;
    }

    public Dir turnRight() {
        return new Dir((num + 1) % 4);
    }

    public Dir turnLeft() {
        return new Dir((num + 3) % 4);
    }

    public Dir flip() {
        return new Dir((num + 2) % 4);
    }

    public R2 advance(R2 point, int amount) {
        return switch (num) {
            case 0 -> new R2(point.x(), point.y() - amount);
            case 1 -> new R2(point.x() + amount, point.y());
            case 2 -> new R2(point.x(), point.y() + amount);
            case 3 -> new R2(point.x() - amount, point.y());
            default -> throw new IllegalStateException("Unexpected value: " + num);
        };
    }
    public R2 advance(R2 point) {
        return advance(point, 1);
    }

    public R2L advance(R2L point, long amount) {
        return switch (num) {
            case 0 -> new R2L(point.x(), point.y() - amount);
            case 1 -> new R2L(point.x() + amount, point.y());
            case 2 -> new R2L(point.x(), point.y() + amount);
            case 3 -> new R2L(point.x() - amount, point.y());
            default -> throw new IllegalStateException("Unexpected value: " + num);
        };
    }
    public R2L advance(R2L point) {
        return advance(point, 1);
    }

    public static Dir fromDelta(R2 delta) {
        var x = delta.x();
        var y = delta.y();
        if (x == 0) {
            if (y == -1) return Dir.Up;
            else if (y == 1) return Dir.Down;
            else return null;
        }
        else if (y == 0) {
            if (x == -1) return Dir.Left;
            else if (x == 1) return Dir.Right;
            else return null;
        }
        else return null;
    }

    public boolean equals(Dir other) {
        return num == other.num;
    }

    public String toString() {
        return switch (num) {
            case 0 -> "Up";
            case 1 -> "Right";
            case 2 -> "Down";
            case 3 -> "Left";
            default -> "R(" + num + ")";
        };
    }

    public static Dir fromChar(char c) {
        return switch(c) {
            case 'U' -> Dir.Up;
            case 'R' -> Dir.Right;
            case 'D' -> Dir.Down;
            case 'L' -> Dir.Left;
            case '^' -> Dir.Up;
            case '>' -> Dir.Right;
            case 'V' -> Dir.Down;
            case 'v' -> Dir.Down;
            case '<' -> Dir.Left;
            default -> null;
        };
    }

}
