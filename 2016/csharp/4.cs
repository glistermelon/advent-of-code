using MoreLinq.Extensions;

class Day4 {

    public static string Decrypt(string line) {
        int sector = int.Parse(line[(line.Length - 3)..]);
        return new(
            line[..(line.Length - 4)].Replace("-", " ")
                .Select(c => {
                    if (c == ' ') {
                        return ' ';
                    }
                    int i = (int)c;
                    return (char)((((i - 'a') + sector) % 26) + 'a');
                })
                .ToArray()
        );
    }

    public static void Solve() {

        int output1 = 0;
        int output2 = 0;
        List<string> real = [];
        foreach (string line in Utility.GetInputString(4).Split("\n")) {
            int checksumIndex = line.IndexOf('[') + 1;
            string checksum = line[checksumIndex..(checksumIndex + 5)];
            //string[] split = line[..(checksumIndex - 3)].Split("-");
            string sorted = new(
                line[..(checksumIndex - 4)]
                    .Replace("-", "")
                    .GroupBy(c => c)
                    .GroupBy(g => g.Count())
                    .OrderBy(gg => gg.First().Count())
                    .Select(
                        gg => gg.OrderBy(g => g.Key)
                            .Reverse()
                            .Select(g => g.Key)
                    )
                    .Flatten()
                    .Select(s => (char)s)
                    .Reverse()
                    .Take(5)
                    .ToArray()
            );
            if (sorted == checksum) {
                output1 += int.Parse(line[(checksumIndex - 4)..(checksumIndex - 1)]);
                real.Add(line);
                if ("northpole object storage" == Decrypt(line[..(checksumIndex - 1)])) {
                    output2 = int.Parse(line[(checksumIndex - 4)..(checksumIndex - 1)]);
                }
            }
        }

        Console.WriteLine(output1);
        Console.WriteLine(output2);

    }

}