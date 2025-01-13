class Day17 {

    static byte[] HashMD5(string str) {
        return System.Security.Cryptography.MD5.HashData(System.Text.Encoding.ASCII.GetBytes(str));
    }

    public static void Solve() {

        string input = Utility.GetInputString(17);
        Queue<(string path, (int x, int y) pos)> queue = new();
        queue.Enqueue(("", (0, 0)));
        int longest = 0;
        bool shortestFound = false;
        while (queue.Count != 0) {
            var (path, pos) = queue.Dequeue();
            var hashBytes = HashMD5(input + path);
            int[] hash = [ hashBytes[0] >> 4, hashBytes[0] & 0b1111, hashBytes[1] >> 4, hashBytes[1] & 0b1111 ];
            Direction[] directions = [ Direction.Up, Direction.Down, Direction.Left, Direction.Right ];
            foreach (var it in directions.Select((d, i) => new { dir=d, idx=i })) {
                if (hash[it.idx] <= 10) continue;
                (int x, int y) adj = it.dir.Advance(pos);
                if (adj.x < 0 || adj.y < 0 || adj.x > 3 || adj.y > 3) continue;
                string newPath = path + it.dir.ToLetter();
                if (adj == (3, 3)) {
                    if (!shortestFound) {
                        shortestFound = true;
                        Console.WriteLine(newPath);
                    }
                    if (newPath.Length > longest) longest = newPath.Length;
                    continue;
                }
                queue.Enqueue((newPath, adj));
            }
        }
        Console.WriteLine(longest);

    }

}