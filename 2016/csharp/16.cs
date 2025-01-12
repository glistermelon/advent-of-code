class Day16 {

    public static void Solve() {

        foreach (var diskLen in (int[])[272, 35651584]) {

            List<bool> bits = [];
            foreach (char c in Utility.GetInputString(16)) {
                bits.Add(c == '1');
            }
            while (bits.Count < diskLen) {
                bits.Add(false);
                bits.AddRange(bits[..^1].Select(b => !b).Reverse());
            }
            bits.RemoveRange(diskLen, bits.Count - diskLen);
            while (bits.Count % 2 == 0) {
                bits = bits.Chunk(2).Select(chunk => chunk[0] == chunk[1]).ToList();
            }

            Console.WriteLine(string.Join("", bits.Select(b => b ? '1' : '0')));

        }
        
    }

}