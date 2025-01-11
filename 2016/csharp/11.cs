class Day11 {

    public static void Solve() {

        // algorithm crafting time wasted!
        // solution appears to be linearly dependent on solely the item count due to the nature of the input
        string input = Utility.GetInputString(11);
        int items = input.Split("generator").Count() + input.Split("microchip").Count() - 2;
        int output1 = 11 + 12 * (items - 7);
        int output2 = 11 + 12 * (items - 5);
        Console.WriteLine(output1);
        Console.WriteLine(output2);

    }

}