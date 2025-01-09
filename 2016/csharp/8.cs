class Day8 {

    public static void Solve() {

        var screen = new bool[50, 6];
        foreach (string instr in Utility.GetInputString(8).Split("\n")) {
            if (instr.StartsWith("rect")) {
                int[] nums = instr[5..].Split("x").Select(int.Parse).ToArray();
                for (int x = 0; x < nums[0]; x++) {
                    for (int y = 0; y < nums[1]; y++) {
                        screen[x, y] = true;
                    }
                }
            }
            else if (instr.StartsWith("rotate row")) {
                int[] nums = instr[13..].Split(" by ").Select(int.Parse).ToArray();
                bool[] row = new bool[50];
                for (int x = 0; x < 50; x++) {
                    row[x] = screen[x, nums[0]];
                }
                for (int x = 0; x < 50; x++) {
                    screen[(x + nums[1]) % 50, nums[0]] = row[x];
                }
            }
            else if (instr.StartsWith("rotate column")) {
                int[] nums = instr[16..].Split(" by ").Select(int.Parse).ToArray();
                bool[] col = new bool[6];
                for (int y = 0; y < 6; y++) {
                    col[y] = screen[nums[0], y];
                }
                for (int y = 0; y < 6; y++) {
                    screen[nums[0], (y + nums[1]) % 6] = col[y];
                }
            }
        }

        int output1 = 0;
        foreach (bool light in screen) {
            if (light) {
                output1++;
            }
        }
        
        Console.WriteLine(output1);

        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 50; x++) {
                Console.Write(screen[x, y] ? '#' : ' ');
            }
            Console.WriteLine();
        }

    }

}