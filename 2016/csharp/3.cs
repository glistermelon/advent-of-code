using MoreLinq.Extensions;

class Day3 {

    public static void Solve() {

        string input = Utility.GetInputString(3);
        while (input.Contains("  ")) {
            input = input.Replace("  ", " ");
        }

        var lines = input.Split("\n");

        int output1 = lines.Length;
        List<int> allNums = [];
        
        foreach (string line in lines) {
            var nums = line[1..].Split(" ").Select(int.Parse).ToArray();
            allNums.AddRange(nums);
            foreach (var combo in nums.Permutations()) {
                if (combo[0] + combo[1] <= combo[2]) {
                    output1--;
                    break;
                }
            }
        }
        
        int output2 = allNums.Count / 3;
        for (int i = 0; i < allNums.Count; i += 9) {
            for (int j = 0; j < 3; j++) {
                int k = i + j;
                int[] nums = [allNums[k], allNums[k + 3], allNums[k + 6]];
                foreach (var combo in nums.Permutations()) {
                    if (combo[0] + combo[1] <= combo[2]) {
                        output2--;
                        break;
                    }
                }
            }
        }

        Console.WriteLine(output1);
        Console.WriteLine(output2);

    }

}