class U64Range(ulong start, ulong end) {
    public ulong Start = start;
    public ulong End = end;
}

class RangeSet {

    public List<U64Range> Ranges { get; } = [];

    public void AddRange(U64Range newRange) {
        if (Ranges.Any(r => r.Start <= newRange.Start && r.End >= newRange.End)) return;
        Ranges.RemoveAll(r => r.Start >= newRange.Start && r.End <= newRange.End);
        U64Range[] overlap = Ranges.Where(
            r =>
                (r.Start >= newRange.Start && r.Start <= newRange.End)
                || (r.End >= newRange.Start && r.End <= newRange.End)
        ).ToArray();
        if (overlap.Length == 0) {
            Ranges.Add(newRange);
        }
        else {
            foreach (var range in overlap) Ranges.Remove(range);
            var start = Utility.Min(overlap.Select(r => r.Start).Min(), newRange.Start);
            var end = Utility.Max(overlap.Select(r => r.End).Min(), newRange.End);
            Ranges.Add(new(start, end));
        }
    }

    public void RemoveRange(U64Range newRange) {

        Ranges.RemoveAll(r => r.Start >= newRange.Start && r.End <= newRange.End);

        foreach (var r in Ranges) {
            if (r.Start <= newRange.Start && r.End >= newRange.End) {
                Ranges.Remove(r);
                if (newRange.End != r.End)
                    Ranges.Add(new(newRange.End + 1, r.End));
                if (newRange.Start != r.Start)
                    Ranges.Add(new(r.Start, newRange.Start - 1));
                return;
            }
        }

        foreach (var r in Ranges) {
            if (r.Start >= newRange.Start && r.Start <= newRange.End) {
                Ranges.Remove(r);
                Ranges.Add(new(newRange.End + 1, r.End));
                break;
            }
        }

        foreach (var r in Ranges) {
            if (r.End >= newRange.Start && r.End <= newRange.End) {
                Ranges.Remove(r);
                Ranges.Add(new(r.Start, newRange.Start - 1));
                break;
            }
        }

    }

}

class Day20 {

    public static void Solve() {

        RangeSet ranges = new();
        ranges.AddRange(new(0, 4294967295));
        foreach (string ln in Utility.GetInputString(20).Split("\n")) {
            uint[] range = ln.Split("-").Select(uint.Parse).ToArray();
            ranges.RemoveRange(new(range[0], range[1]));
        }
        Console.WriteLine(ranges.Ranges.Select(r => r.Start).Min());
        Console.WriteLine(ranges.Ranges.Select(r => r.End - r.Start + 1).Aggregate((a, b) => a + b));

    }

}