using MoreLinq;

class Day24 {

    public static void Solve() {

        var input = Utility.GetInputString(24).Split("\n").Select(row => row.ToArray()).ToArray();
        List<(int, int)> goals = [];
        (int, int) start = (-1, -1);
        List<(int, int)> grid = [];
        for (int y = 0; y < input.Length; y++) {
            var row = input[y];
            for (int x = 0; x < row.Length; x++) {
                char c = row[x];
                if (c >= '1' && c <= '9') goals.Add((y, x));
                else if (c == '0') start = (y, x);
                if (c != '#') grid.Add((y, x));
            }
        }
        goals.Add(start);

        Dictionary<((int, int), (int, int)), int> distances = [];
        Dictionary<(int, int), int> scores = [];
        foreach (var pair in goals.Subsets(2)) {
            foreach (var p in grid) scores[p] = int.MaxValue;
            scores[pair[0]] = 0;
            List<(int, int)> unvisited = new(grid);
            while (true) {
                var p = Enumerable.MinBy(unvisited, p => scores[p]);
                unvisited.Remove(p);
                var s = scores[p];
                if (p == pair[1]) {
                    distances[(pair[0], pair[1])] = s;
                    distances[(pair[1], pair[0])] = s;
                    break;
                }
                foreach (var dir in Direction.Directions) {
                    var a = dir.Advance(p);
                    if (!unvisited.Contains(a)) continue;
                    var s1 = s + 1;
                    if (s1 < scores[a]) scores[a] = s1;
                }
            }
        }

        foreach (var part1 in (bool[])[true, false]) {
            int output = int.MaxValue;
            goals.Remove(start);
            foreach (var path in goals.Permutations()) {
                List<(int, int)> fullPath = [start];
                fullPath.AddRange(path);
                if (!part1) fullPath.Add(start);
                int len = Enumerable.Range(0, fullPath.Count - 1)
                    .Select(i => distances[(fullPath[i], fullPath[i + 1])])
                    .Sum();
                if (len < output) output = len;
            }
            Console.WriteLine(output);
        }

    }

}