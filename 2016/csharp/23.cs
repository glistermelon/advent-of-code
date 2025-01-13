class Day23 {

    public static void Solve() {

        List<string>[] instructions = Utility.GetInputString(23)
            .Split("\n")
            .Select(s => s.Split(" ").ToList())
            .ToArray();
        Console.WriteLine(
            int.Parse(instructions[19][1]) * int.Parse(instructions[20][1]) + 5040
        );
        Console.WriteLine(
            int.Parse(instructions[19][1]) * int.Parse(instructions[20][1]) + 479001600
        );

    }

}