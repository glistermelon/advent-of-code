using System.Text;
using System.Text.RegularExpressions;

class Day2 {

    static int NumAtPos1((int x, int y) pos) {
        return 2 + pos.x + 3 * (pos.y + 1);
    }    

    public static void Part1() {

        (int x, int y) pos = (0, 0);
        InclusiveBound2D bound = new(-1, 1, -1, 1);
        List<int> buttons = [];

        foreach (string line in Utility.GetInputString(2).Split("\n")) {
            foreach (char c in line) {
                pos = Direction.FromChar(c).Advance(pos, 1, bound);
            }
            buttons.Add(NumAtPos1(pos));
        }

        string output1 = string.Join("", buttons);

        Console.WriteLine(output1);

    }

    static char CharAtPos2((int x, int y) pos) {
        return pos switch {
            (0, -2) => '1',
            (-1, -1) => '2',
            (0, -1) => '3',
            (1, -1) => '4',
            (-2, 0) => '5',
            (-1, 0) => '6',
            (0, 0) => '7',
            (1, 0) => '8',
            (2, 0) => '9',
            (-1, 1) => 'A',
            (0, 1) => 'B',
            (1, 1) => 'C',
            (0, 2) => 'D',
            _ => throw new Exception()
        };
    }    

    public static void Part2() {

        (int x, int y) pos = (-2, 0);
        List<char> buttons = [];

        foreach (string line in Utility.GetInputString(2).Split("\n")) {
            foreach (char c in line) {
                var prevPos = pos;
                pos = Direction.FromChar(c).Advance(pos);
                if (Math.Abs(pos.x) + Math.Abs(pos.y) > 2) {
                    pos = prevPos;
                }
            }
            buttons.Add(CharAtPos2(pos));
        }

        string output1 = string.Join("", buttons);

        Console.WriteLine(output1);

    }

    public static void Solve() {
        Part1();
        Part2();
    }

}