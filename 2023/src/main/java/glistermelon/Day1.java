package glistermelon;

import java.io.IOException;

public class Day1 extends DaySolver {

    public Day1() throws IOException {
        super(1);
    }

    public String solvePart1() {
        int sum = 0;
        for (String ln : this.getPuzzleInputLines()) {
            int i = 0, j = ln.length() - 1;
            while (!Character.isDigit(ln.charAt(i))) i++;
            while (!Character.isDigit(ln.charAt(j))) j--;
            sum += Integer.parseInt(ln.charAt(i) + ln.substring(j, j + 1));
        }
        return String.valueOf(sum);
    }

    public String solvePart2() {
        int sum = 0;
        for (String ln : this.getPuzzleInputLines()) {
            int i = 0, j = ln.length() - 1;
            Integer n1, n2;
            do {
                n1 = intAt(ln, i);
                i++;
            } while (n1 == null);
            do {
                n2 = intAt(ln, j);
                j--;
            } while (n2 == null);
            sum += n1 * 10 + n2;
        }
        return String.valueOf(sum);
    }

    public Integer intAt(String ln, int i) {
        try {
            return Integer.parseInt(ln.substring(i, i + 1));
        }
        catch (NumberFormatException _) {
            String[] strings = {
                    "one", "two", "three", "four",
                    "five", "six", "seven", "eight", "nine"
            };
            for (int j = 0; j < strings.length; j++) {
                String s = strings[j];
                if (ln.startsWith(s, i)) {
                    return j + 1;
                }
            }
            return null;
        }
    }

}
