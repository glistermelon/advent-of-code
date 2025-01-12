class Day15 {

    public static void Solve() {

        foreach (var part1 in (bool[])[true, false]) {

            var queue = new PriorityQueue<int, int>();
            int maxPriority = 0;
            foreach (string line in Utility.GetInputString(15).Replace("#", "").Replace(".", "").Split("\n")) {
                string[] split = line.Split(" ").ToArray();
                var discNum = int.Parse(split[1]);
                var positions = int.Parse(split[3]);
                var startPos = int.Parse(split[11]);
                startPos = (startPos + discNum) % positions;
                int priority = startPos == 0 ? 0 : (positions - startPos);
                queue.Enqueue(positions, priority);
                if (priority > maxPriority) maxPriority = priority;
            }

            if (!part1) {
                int startPos = (queue.Count + 1) % 11;
                int priority = startPos == 0 ? 0 : (11 - startPos);
                queue.Enqueue(11, priority);
                if (priority > maxPriority) maxPriority = priority;
            }

            while (true) {
                queue.TryDequeue(out int m, out int p);
                if (p == maxPriority) {
                    Console.WriteLine(p);
                    break;
                }
                p += m;
                if (p > maxPriority) maxPriority = p;
                queue.Enqueue(m, p);
            }

        }

    }

}