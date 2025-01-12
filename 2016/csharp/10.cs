using System.Reflection.Metadata;
using MoreLinq.Extensions;

class Bot {

    static Dictionary<int, Bot> bots = [];
    public static Dictionary<int, int> outputs = [];

    readonly int Id;
    readonly int High;
    readonly int Low;
    readonly bool HighOutput;
    readonly bool LowOutput;
    int? Num;

    public Bot(int id, int high, int low, bool highOut, bool lowOut, int? num) {
        Id = id;
        High = high;
        Low = low;
        Num = num;
        LowOutput = lowOut;
        HighOutput = highOut;
        bots[id] = this;
    }

    public void Give(int num) {
        if (Num != null) {
            int min = Utility.Min(Num.Value, num);
            int max = Utility.Max(Num.Value, num);
            if (min == 17 && max == 61) {
                Console.WriteLine(Id);
            }
            if (LowOutput) {
                outputs[Low] = min;
            }
            else {
                bots[Low].Give(min);
            }
            if (HighOutput) {
                outputs[High] = max;
            }
            else {
                bots[High].Give(max);
            }
            Num = null;
        }
        else {
            Num = num;
        }
    }

    public static Bot GetById(int id) {
        return bots[id];
    }

}

class Day10 {

    static Bot[] Parse() {
        
        List<int>[] startValues = new List<int>[1000];
        for (int i = 0; i < 1000; i++) {
            startValues[i] = [];
        }
        (int high, int low, bool highOut, bool lowOut)[] gives = new (int, int, bool, bool)[1000];
        HashSet<int> ids = [];
        string[] input = [..Utility.GetInputString(10).Split("\n")];
        foreach (string ln in input) {
            if (ln.StartsWith("value")) {
                string[] split = [..ln.Split(" ")];
                int id = int.Parse(split.Last());
                startValues[id].Add(int.Parse(split[1]));
                ids.Add(id);
            }
            else {
                string[] split = [..ln.Split(" ")];
                int id = int.Parse(split[1]);
                gives[id] = (
                    int.Parse(split.Last()), int.Parse(split[6]),
                    split[^2] == "output", split[5] == "output"
                );
                ids.Add(id);
            }
        }
        Bot[] bots = ids.Select(
            id => new Bot(
                id,
                gives[id].high, gives[id].low,
                gives[id].highOut, gives[id].lowOut,
                startValues[id].Count == 0 ? null : startValues[id][0]
            )
        ).ToArray();
        foreach (int id in ids) {
            if (startValues[id].Count > 1) {
                foreach (var v in startValues[id][1..]) {
                    Bot.GetById(id).Give(v);
                }
            }
        }
        return bots;

    }

    public static void Solve() {

        Parse();
        int output2 = Bot.outputs[0] * Bot.outputs[1] * Bot.outputs[2];
        Console.WriteLine(output2);

    }

}