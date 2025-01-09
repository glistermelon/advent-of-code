class Day9 {

    static long Decompress(string data, bool recurse) {

        long output = 0;
        int i = 0;
        while (i != data.Length) {
            long prev = i;
            i = data.IndexOf('(', i);
            if (i == -1) {
                output += data.Length - prev;
                break;
            }
            output += i - prev;
            int j = data.IndexOf(')', i);
            int[] nums = data[(i + 1)..j].Split("x").Select(int.Parse).ToArray();
            i = j + 1;
            output += (recurse ? Decompress(data[i..(i + nums[0])], true) : nums[0]) * nums[1];
            i += nums[0];
        }
        return output;

    }

    public static void Solve() {

        string data = Utility.GetInputString(9);
        Console.WriteLine(Decompress(data, false));
        Console.WriteLine(Decompress(data, true));

    }

}