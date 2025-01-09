class Day6 {

    public static void Solve() {

        string[] input = Utility.GetInputString(6).Split("\n").ToArray();
        char[] columns1 = new char[input[0].Length];
        char[] columns2 = new char[columns1.Length];
        for (int i = 0; i < input[0].Length; i++) {
            columns1[i] = input.Select(ln => ln[i])
                .GroupBy(c => c)
                .OrderBy(g => g.Count())
                .Last()
                .Key;
            columns2[i] = input.Select(ln => ln[i])
                .GroupBy(c => c)
                .OrderBy(g => g.Count())
                .First()
                .Key;
        }

        string output1 = new(columns1);
        string output2 = new(columns2);

        Console.WriteLine(output1);
        Console.WriteLine(output2);

    }

}