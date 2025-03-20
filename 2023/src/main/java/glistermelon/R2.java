package glistermelon;

public record R2(int x, int y) {

    public static R2 find(char[][] map, char c) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] == c) return new R2(x, y);
            }
        }
        return null;
    }

    public Character getChar(char[][] map) {
        try {
            return map[y][x];
        }
        catch (Exception _) {
            return null;
        }
    }

    public boolean equals(R2 other) {
        return other != null && x == other.x && y == other.y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public R2[] adjacent() {
        return new R2[]{new R2(x + 1, y), new R2(x - 1, y), new R2(x, y + 1), new R2(x, y - 1)};
    }

    public int taxiDist(R2 other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }

    public R2 add(R2 other) {
        return new R2(x + other.x, y + other.y);
    }

    public R2 sub(R2 other) {
        return new R2(x - other.x, y - other.y);
    }

}