using System.Text;

class Utility {

    public static string GetInputPath(int day) {
        return "../../../../inputs/" + day + ".txt";
    }

    public static string GetInputString(int day) {
        return File.ReadAllText(GetInputPath(day)).Replace("\r", "");
    }

    public static int Max(int a, int b) {
        return a > b ? a : b;
    }

    public static int Min(int a, int b) {
        return a < b ? a : b;
    }

    public static ulong Max(ulong a, ulong b) {
        return a > b ? a : b;
    }

    public static ulong Min(ulong a, ulong b) {
        return a < b ? a : b;
    }

    public static int ClampExclusive(int n, int a, int b) {
        if (n <= a) return a + 1;
        if (n >= b) return b - 1;
        return n;
    }

    public static int ClampInclusive(int n, int a, int b) {
        if (n <= a) return a;
        if (n >= b) return b;
        return n;
    }

}

class Bound2D {

    public int MinX;
    public int MaxX;
    public int MinY;
    public int MaxY;

    public Bound2D(int minX, int maxX, int minY, int maxY, bool inclusive) {
        if (!inclusive) {
            minX += 1;
            maxX -= 1;
            minY += 1;
            maxY -= 1;
        }
        MinX = minX;
        MaxX = maxX;
        MinY = minY;
        MaxY = maxY;
    }

    public (int, int) Clamp((int x, int y) p) {
        return (Utility.ClampInclusive(p.x, MinX, MaxX), Utility.ClampInclusive(p.y, MinY, MaxY));
    }

}


class InclusiveBound2D : Bound2D {
    public InclusiveBound2D(int minX, int maxX, int minY, int maxY) : base(minX, maxX, minY, maxY, true) {}
}

class ExclusiveBound2D : Bound2D {
    public ExclusiveBound2D(int minX, int maxX, int minY, int maxY) : base(minX, maxX, minY, maxY, false) {}
}