class Day13 {

    static int BitCount(uint n) {
        int bits = 0;
        while (n != 0) {
            if ((n & 1) == 1) bits++;
            n >>= 1;
        }
        return bits;
    }

    public static void Solve() {

        int input = int.Parse(Utility.GetInputString(13));
        List<(int x, int y)> queue = [(1, 1)];
        List<(int, int)> visited = [queue[0]];
        int steps = 0;
        bool output1Pending = true;
        int output2 = 0;
        while (output1Pending) {
            steps++;
            List<(int, int)> nextQueue = [];
            foreach (var pos in queue) {
                foreach (var (dx, dy) in ((int dx, int dy)[])[(0, 1), (0, -1), (1, 0), (-1, 0)]) {
                    var x = pos.x + dx;
                    var y = pos.y + dy;
                    if (
                        x >= 0 && y >= 0 &&
                        BitCount((uint)(x*x + 3*x + 2*x*y + y + y*y + input)) % 2 == 0 &&
                        !visited.Contains((x, y))
                    ) {
                        if (x == 31 && y == 39) {
                            Console.WriteLine(steps);
                            output1Pending = false;
                            break;
                        }
                        nextQueue.Add((x, y));
                        visited.Add((x, y));
                    }
                }
                if (!output1Pending) break;
            }
            queue = nextQueue;
            if (steps == 50) output2 = visited.Count;
        }
        Console.WriteLine(output2);

    }

}