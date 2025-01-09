using System.Globalization;

class Day5 {

    static byte[] HashMD5(string str) {
        return System.Security.Cryptography.MD5.HashData(System.Text.Encoding.ASCII.GetBytes(str));
    }

    static bool HashIsInteresting(byte[] hash) {
        return hash[0] == 0 && hash[1] == 0 && hash[2] >> 4 == 0;
    }

    public static void SolvePart(bool part1) {
        string input = Utility.GetInputString(5);
        int hashIndex = 0;
        int pwIndex = 0;
        int charsSet = 0;
        char[] password = [.."\0\0\0\0\0\0\0\0"];
        while (charsSet != password.Length) {
            var hash = HashMD5(input + hashIndex.ToString());
            if (HashIsInteresting(hash)) {
                if (part1) {
                    int quartet = hash[2];
                    password[pwIndex++] = quartet >= 10 ? (char)('a' + quartet - 10) : (char)('0' + quartet);
                    charsSet++;
                }
                else {
                    pwIndex = hash[2];
                    if (pwIndex < password.Length && password[pwIndex] == '\0') {
                        int quartet = hash[3] >> 4;
                        password[pwIndex] = quartet >= 10 ? (char)('a' + quartet - 10) : (char)('0' + quartet);
                        charsSet++;
                    }
                }
            }
            hashIndex++;
        }
        string output1 = new(password);
        Console.WriteLine(output1);
    }

    public static void Solve() {
        SolvePart(true);
        SolvePart(false);
    }

}