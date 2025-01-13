using MoreLinq;

class Node((int, int) pos, int used, int avail) {

    public (int x, int y) Pos = pos;
    public int Used = used;
    public int Avail = avail;

    public bool Equals(Node other) {
        return other.Pos == Pos;
    }

}

class Day22 {

    static List<(int, int)> Grid = [];

    static int Distance((int, int) fullNode, (int, int) empty, (int, int) currentPos) {
        List<(int, int)> queue = [fullNode];
        List<(int, int)> visited = [fullNode];
        int steps = 1;
        while (true) {
            List<(int, int)> nextQueue = [];
            foreach (var node in queue) {
                foreach (var adj in Direction.Directions.Select(d => d.Advance(node)).Where(Grid.Contains).ToList()) {
                    if (adj == currentPos || visited.Contains(adj)) continue;
                    if (adj == empty) return steps;
                    nextQueue.Add(adj);
                    visited.Add(adj);
                }
            }
            steps++;
            queue = nextQueue;
        }
    }

    struct SearchNode((int, int) pos, int step, (int, int) empty) {
        public (int, int) Pos = pos;
        public int Step = step;
        public (int, int) Empty = empty;
    }

    static int Search((int, int) startNode, (int, int) endNode, (int, int) initialEmptyNode) {
        List<SearchNode> queue = [new(startNode, 0, initialEmptyNode)];
        List<(int, int)> visited = [startNode];
        while (true) {
            List<SearchNode> nextQueue = [];
            foreach (var s in queue) {
                foreach (var adj in Direction.Directions.Select(d => d.Advance(s.Pos)).Where(Grid.Contains).ToList()) {
                    if (visited.Contains(adj)) continue;
                    int newStep = s.Step + 1 + Distance(adj, s.Empty, s.Pos);
                    if (adj == endNode) return newStep;
                    nextQueue.Add(new(adj, newStep, s.Pos));
                    visited.Add(adj);
                }
            }
            queue = nextQueue;
        }
    }

    public static void Solve() {

        List<Node> nodes = [];
        foreach (string line in Utility.GetInputString(22).Split("\n")) {
            if (line[0] != '/') continue;
            string line1 = line.Replace("T", "").Replace("x", "").Replace("y", "").Replace("-", " ");
            while (line1.Contains("  ")) line1 = line1.Replace("  ", " ");
            string[] split = line1.Split(" ");
            nodes.Add(new((int.Parse(split[1]), int.Parse(split[2])), int.Parse(split[4]), int.Parse(split[5])));
        }
        Console.WriteLine(
            nodes.Cartesian(nodes, (a, b) => a.Used != 0 && !a.Equals(b) && a.Used <= b.Avail ? 1 : 0).Sum()
        );

        nodes.RemoveAll(n => n.Used > 100);
        Grid = nodes.Select(n => n.Pos).ToList();
        Console.WriteLine(Search(
            nodes.Where(n => n.Pos.y == 0).OrderBy(n => n.Pos.x).Reverse().First().Pos,
            nodes.Where(n => n.Pos.y == 0).OrderBy(n => n.Pos.x).First().Pos,
            nodes.Where(n => n.Used == 0).First().Pos
        ));

    }

}