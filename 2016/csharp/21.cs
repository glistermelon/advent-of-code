class Day21 {

    public static void Solve() {

        string[] instructions = Utility.GetInputString(21).Split("\n").ToArray();

        string pw = "abcdefgh";
        foreach (string instr in instructions) {
            string[] split = instr.Split(" ").ToArray();
            List<int> nums = [];
            foreach (string s in split) {
                try {
                    nums.Add(int.Parse(s));
                }
                catch {}
            }
            List<int> sorted = new(nums.Order());
            if (instr.StartsWith("swap position"))
                pw = pw[..sorted[0]] + pw[sorted[1]] + pw[(sorted[0] + 1)..sorted[1]] + pw[sorted[0]] + pw[(sorted[1] + 1)..];
            else if (instr.StartsWith("swap letter"))
                pw = pw.Replace(split[2][0], '0').Replace(split[5][0], split[2][0]).Replace('0', split[5][0]);
            else if (instr.StartsWith("rotate left"))
                pw = new(Enumerable.Range(0, pw.Length).Select(i => pw[(i + sorted[0]) % pw.Length]).ToArray());
            else if (instr.StartsWith("rotate right"))
                pw = new(Enumerable.Range(0, pw.Length).Select(i => pw[(i - sorted[0] + pw.Length) % pw.Length]).ToArray());
            else if (instr.StartsWith("rotate based")) {
                int rot = pw.IndexOf(split[6][0]);
                if (rot >= 4) rot++;
                rot++;
                pw = new(Enumerable.Range(0, pw.Length).Select(i => pw[(i - rot + 2 * pw.Length) % pw.Length]).ToArray());
            }
            else if (instr.StartsWith("reverse"))
                pw = pw[..sorted[0]] + new string(pw[sorted[0]..(sorted[1] + 1)].Reverse().ToArray()) + pw[(sorted[1] + 1)..];
            else if (instr.StartsWith("move"))
                pw = pw.Remove(nums[0], 1).Insert(nums[1], pw[nums[0]].ToString());
        }
        Console.WriteLine(pw);

        pw = "fbgdceah";
        foreach (string instr in instructions.Reverse()) {
            string[] split = instr.Split(" ").ToArray();
            List<int> nums = [];
            foreach (string s in split) {
                try {
                    nums.Add(int.Parse(s));
                }
                catch {}
            }
            List<int> sorted = new(nums.Order());
            if (instr.StartsWith("swap position"))
                pw = pw[..sorted[0]] + pw[sorted[1]] + pw[(sorted[0] + 1)..sorted[1]] + pw[sorted[0]] + pw[(sorted[1] + 1)..];
            else if (instr.StartsWith("swap letter"))
                pw = pw.Replace(split[2][0], '0').Replace(split[5][0], split[2][0]).Replace('0', split[5][0]);
            else if (instr.StartsWith("rotate left"))
                pw = new(Enumerable.Range(0, pw.Length).Select(i => pw[(i - sorted[0] + pw.Length) % pw.Length]).ToArray());
            else if (instr.StartsWith("rotate right"))
                pw = new(Enumerable.Range(0, pw.Length).Select(i => pw[(i + sorted[0]) % pw.Length]).ToArray());
            else if (instr.StartsWith("rotate based")) {
                pw = new(Enumerable.Range(0, pw.Length).Select(i => pw[(i + 1) % pw.Length]).ToArray());
                int index = pw.IndexOf(split[6][0]);
                int rot = index % 2 == 0 ? (index / 2) : ((index - 1 + pw.Length) / 2 + 1);
                pw = new(Enumerable.Range(0, pw.Length).Select(i => pw[(i + rot) % pw.Length]).ToArray());
            }
            else if (instr.StartsWith("reverse"))
                pw = pw[..sorted[0]] + new string(pw[sorted[0]..(sorted[1] + 1)].Reverse().ToArray()) + pw[(sorted[1] + 1)..];
            else if (instr.StartsWith("move"))
                pw = pw.Remove(nums[1], 1).Insert(nums[0], pw[nums[1]].ToString());
        }
        Console.WriteLine(pw);

    }

}