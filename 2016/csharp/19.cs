class Day19 {

    public static void Solve() {

        int count = int.Parse(Utility.GetInputString(19));
        var elves = Enumerable.Range(1, count).ToList();
        while (elves.Count != 1) {
            var nextElves = elves.Where((g, i) => i % 2 == 0).ToList();
            if (elves.Count % 2 != 0) nextElves.RemoveAt(0);
            elves = nextElves;
        }
        Console.WriteLine(elves[0]);

        // I don't care enough to work out the intended solution
        elves = Enumerable.Range(1, count).ToList();
        int activeElf = 1;
        while (true) {
            elves.RemoveAt((elves.IndexOf(activeElf) + elves.Count / 2) % elves.Count);
            if (elves.Count == 1) break;
            activeElf = elves[(elves.IndexOf(activeElf) + 1) % elves.Count];
        }
        Console.WriteLine(elves[0]);

    }

}