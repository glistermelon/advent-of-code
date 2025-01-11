using System.Collections;
using MoreLinq;

class RotatableArray<T>(T[] data) : IEnumerable<T> {

    private T[] Data = data;
    private int Rot = 0;
    public int Length => Data.Length;

    public T this[int i] {
        get {
            if (i >= Data.Length) throw new Exception();
            return Data[(i + Rot) % Data.Length];
        }
        set {
            if (i >= Data.Length) throw new Exception();
            Data[(i + Rot) % Data.Length] = value;
        }
    }

    public IEnumerator<T> GetEnumerator() {
        for (int i = 0; i < Data.Length; i++)
            yield return Data[(i + Rot) % Data.Length];
    }
    
    IEnumerator IEnumerable.GetEnumerator() {
        return GetEnumerator();
    }

    public void RotateRight(int n = 1) {
        Rot -= n;
    }

    public void RotateLeft(int n = 1) {
        Rot += n;
    }

}

class Day14 {

    static string HashMD5(string str, int stretch) {
        string hash = str;
        for (int i = 0; i < stretch; i++)
            hash = Convert.ToHexStringLower(System.Security.Cryptography.MD5.HashData(System.Text.Encoding.ASCII.GetBytes(hash)));
        return hash;
    }

    static char FindGroup(string str, int n, char? predeterminedC = null) {
        for (int i = 0; i < str.Length - n + 1; i++) {
            if (predeterminedC != null && str[i] != predeterminedC) continue;
            if (str[(i + 1)..(i + n)].All(c => c == str[i])) {
                return str[i];
            }
        }
        return '\0';
    }

    public static void Solve() {

        string salt = Utility.GetInputString(14);
        for (int stretch = 1; stretch <= 2017; stretch += 2016) {
            RotatableArray<string> hashes = new(Enumerable.Range(0, 1001).Select(n => HashMD5(salt + n.ToString(), stretch)).ToArray());
            int keys = 0;
            int keyIndex = 0;
            while (keys != 64) {
                char c = FindGroup(hashes[0], 3);
                if (c != '\0' && hashes.Reverse().Take(1000).Any(h => FindGroup(h, 5, c) != '\0')) {
                    keys++;
                }
                keyIndex++;
                hashes.RotateLeft();
                hashes[^1] = HashMD5(salt + (keyIndex + 1000).ToString(), stretch);
            }
            int output1 = keyIndex - 1;
            Console.WriteLine(output1);
        }

    }

}