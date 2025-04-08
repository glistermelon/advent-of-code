package glistermelon;

public record R2L(long x, long y) {

    public static R2L find(char[][] map, char c) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] == c) return new R2L(x, y);
            }
        }
        return null;
    }

    public Character getChar(char[][] map) {
        try {
            return map[(int)y][(int)x];
        }
        catch (Exception _) {
            return null;
        }
    }

    public boolean equals(R2L other) {
        return other != null && x == other.x && y == other.y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public R2L[] adjacent() {
        return new R2L[]{new R2L(x + 1, y), new R2L(x - 1, y), new R2L(x, y + 1), new R2L(x, y - 1)};
    }

    public long taxiDist(R2L other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }

    public R2L add(R2L other) {
        return new R2L(x + other.x, y + other.y);
    }

    public R2L sub(R2L other) {
        return new R2L(x - other.x, y - other.y);
    }

}