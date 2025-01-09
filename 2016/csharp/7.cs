class Day7 {

    static void Part1() {

        int output = 0;
        bool inSquareBrackets = false;
        foreach (string ipIter in Utility.GetInputString(7).Split("\n")) {
            string ip = ipIter;
            bool validSetFound = false;
            for (int i = 0; i < ip.Length - 3; i++) {
                if (ip[i] == '[') {
                    inSquareBrackets = true;
                    continue;
                }
                else if (ip[i] == ']') {
                    inSquareBrackets = false;
                    continue;
                }
                if (ip[i] == ip[i + 3] && ip[i + 1] == ip[i + 2] && ip[i] != ip[i + 1]) {
                    if (inSquareBrackets) {
                        validSetFound = false;
                        break;
                    }
                    else {
                        validSetFound = true;
                    }
                }
            }
            if (validSetFound) {
                output++;
            }
        }
        Console.WriteLine(output);
        
    }

    static void Part2() {

        int output = 0;
        bool inSquareBrackets = false;
        foreach (string ip in Utility.GetInputString(7).Split("\n")) {
            List<char> insideChars = [];
            List<char> outsideChars = [];
            foreach (char c in ip) {
                if (c == '[') {
                    inSquareBrackets = true;
                    continue;
                }
                else if (c == ']') {
                    inSquareBrackets = false;
                    continue;
                }
                if (inSquareBrackets) {
                    insideChars.Add(c);
                }
                else {
                    outsideChars.Add(c);
                }
            }
            string inside = new([..insideChars]);
            string outside = new([..outsideChars]);
            for (int i = 0; i < outside.Length - 2; i++) {
                if (outside[i] == outside[i + 2] && outside[i] != outside[i + 1] && inside.Contains(new string([outside[i + 1], outside[i], outside[i + 1]]))) {
                    output++;
                    break;
                }
            }
        }
        Console.WriteLine(output);
        
    }

    public static void Solve() {
        Part1();
        Part2();
    }

}