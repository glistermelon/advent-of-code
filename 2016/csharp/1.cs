using System.IO;
using System.Text;

class Day1 {

    public static void Solve() {

        int output2 = -1;

        (int x, int y) pos = (0, 0);
        Direction dir = Direction.Up;

        List<(int, int)> visited = [];
        foreach (string instr in Utility.GetInputString(1).Split(", ")) {
            if (instr.First() == 'R') dir = dir.TurnRight();
            else dir = dir.TurnLeft();
            int m = int.Parse(instr[1..]);
            for (int i = 0; i < m; i++) {
                pos = dir.Advance(pos);
                if (output2 == -1 && visited.Contains(pos)) {
                    output2 = Math.Abs(pos.x) + Math.Abs(pos.y);
                }
                visited.Add(pos);
            }
        }

        int output1 = Math.Abs(pos.x) + Math.Abs(pos.y);

        Console.WriteLine(output1);
        Console.WriteLine(output2);

    }

}