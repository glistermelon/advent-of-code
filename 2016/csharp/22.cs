using MoreLinq;

class Node((int, int) pos, int used, int avail) {
    public (int x, int y) Pos = pos;
    public int Used = used;
    public int Avail = avail;
}

class Day22 {

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
            nodes.Cartesian(nodes, (a, b) => a.Used != 0 && !ReferenceEquals(a, b) && a.Used <= b.Avail ? 1 : 0).Sum()
        );

        

    }

}