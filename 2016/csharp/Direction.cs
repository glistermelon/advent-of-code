using System.Numerics;

class Direction {

    int Num;

    Direction(int num) {
        Num = num;
    }

    public static Direction Up = new(0);
    public static Direction Right = new(1);
    public static Direction Down = new(2);
    public static Direction Left = new(3);

    public static Direction[] Directions = [ Up, Right, Down, Left ];

    public (int, int) Advance((int x, int y) point, int delta, Bound2D? bound) {
        point = Num switch {
            0 => (point.x, point.y - delta),
            1 => (point.x + delta, point.y),
            2 => (point.x, point.y + delta),
            3 => (point.x - delta, point.y),
            _ => throw new Exception(),
        };
        if (bound != null) {
            point = bound.Clamp(point);
        }
        return point;
    }
    public (int, int) Advance((int, int) point) {
        return Advance(point, 1, null);
    }

    public Direction TurnRight() {
        return new((Num + 1) % 4);
    }

    public Direction TurnLeft() {
        return new((Num + 3) % 4);
    }

    public static Direction FromChar(char c) {
        return c switch {
            'U' => Up,
            'R' => Right,
            'D' => Down,
            'L' => Left,
            '^' => Up,
            '>' => Right,
            'v' => Down,
            'V' => Down,
            '<' => Left,
            _ => throw new Exception()
        };
    }

    public char ToLetter() {
        return "URDL"[Num];
    }

}