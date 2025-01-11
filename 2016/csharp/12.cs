class Day12 {

    public static void Solve() {

        List<string>[] instructions = Utility.GetInputString(12)
            .Split("\n")
            .Select(s => s.Split(" ").ToList())
            .ToArray();
        for (int c = 0; c <= 1; c++) {
            int[] registers = [0, 0, c, 0];
            int i = 0;
            while (i >= 0 && i < instructions.Length) {
                if (registers[2] < -100) {
                    break;
                }
                List<string> instr = instructions[i];
                string opcode = instr[0];
                switch (opcode) {
                    case "cpy": {
                        string arg1 = instr[1];
                        int arg2 = instr[2][0] - 'a';
                        if (arg1[0] >= 'a') registers[arg2] = registers[arg1[0] - 'a'];
                        else registers[arg2] = int.Parse(arg1);
                        i++;
                        break;
                    }
                    case "inc":
                        registers[instr[1][0] - 'a']++;
                        i++;
                        break;
                    case "dec":
                        registers[instr[1][0] - 'a'] -= 1;
                        i++;
                        break;
                    case "jnz": {
                        string arg1 = instr[1];
                        if ((arg1[0] >= 'a' ? registers[arg1[0] - 'a'] : int.Parse(arg1)) != 0)
                            i += int.Parse(instr[2]);
                        else i++;
                        break;
                    }
                    default:
                        throw new Exception();
                }
            }
            Console.WriteLine(registers[0]);
        }

    }

}