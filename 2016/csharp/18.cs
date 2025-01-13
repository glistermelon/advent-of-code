using System.Reflection.Metadata;

class Day18 {

    public static void Solve() {

        bool[] firstRow = Utility.GetInputString(18).Select(c => c == '.').ToArray();
        for (int rowCount = 40; rowCount <= 400000; rowCount += 399960) {
            bool[] row = firstRow;
            int safeTiles = row.Where(t => t).Count();
            for (int i = 1; i < rowCount; i++) {
                bool[] nextRow = new bool[row.Length];
                for (int j = 0; j < row.Length; j++) {
                    bool left = j == 0 || row[j - 1];
                    bool center = row[j];
                    bool right = j == row.Length - 1 || row[j + 1];
                    nextRow[j] = !(
                        (!left && !center && right)
                        || (!center && !right && left)
                        || (!left && center && right)
                        || (!right && center && left)
                    );
                }
                row = nextRow;
                safeTiles += row.Where(t => t).Count();
            }
            Console.WriteLine(safeTiles);
        }

    }

}