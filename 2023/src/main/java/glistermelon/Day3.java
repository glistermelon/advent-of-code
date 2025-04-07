package glistermelon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day3 extends DaySolver {

    public Day3() throws IOException {
        super(3);
    }

    Integer[][] numbers;

    public String solvePart1() {

        String[] lines = getPuzzleInputLines();
        numbers = new Integer[lines.length][lines[0].length()];
        int sum = 0;

        for (int r = 0; r < lines.length; r++) {
            String line = lines[r];
            int c0 = 0;
            while (true) {

                while (c0 < line.length() && !Character.isDigit(line.charAt(c0))) c0++;
                if (c0 == line.length()) break;
                int c1 = c0;
                while (c1 < line.length() && Character.isDigit(line.charAt(c1))) c1++;

                boolean valid;
                do {

                    int c2;
                    if (c0 != 0) {
                        c2 = c0 - 1;
                        if (line.charAt(c2) != '.') {
                            valid = true;
                            break;
                        }
                    }
                    else c2 = 0;

                    int c3;
                    if (c1 != line.length()) {
                        if (line.charAt(c1) != '.') {
                            valid = true;
                            break;
                        }
                        c3 = c1 + 1;
                    }
                    else c3 = c1;

                    valid = (r != 0 && lines[r - 1].substring(c2, c3).chars().anyMatch(c -> c != '.'))
                            || (r != lines.length - 1 && lines[r + 1].substring(c2, c3).chars().anyMatch(c -> c != '.'));

                } while (false);
                if (valid) {
                    int n = Integer.parseInt(line.substring(c0, c1));
                    sum += n;
                    for (int c = c0; c < c1; c++) numbers[r][c] = n;
                }

                c0 = c1;

            }
        }

        return String.valueOf(sum);

    }

    public String solvePart2() {

        int sum = 0;

        String[] lines = getPuzzleInputLines();
        for (int r = 0; r < lines.length; r++) {
            String line = lines[r];
            for (int c = 0; c < line.length(); c++) {
                if (line.charAt(c) != '*') continue;
                List<Integer> gearNums = new ArrayList<>();
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        if (dr == 0 && dc == 0) continue;
                        Integer n = numbers[r + dr][c + dc];
                        if (n != null && !gearNums.contains(n)) gearNums.add(n);
                    }
                }
                if (gearNums.size() == 2) sum += gearNums.get(0) * gearNums.get(1);
            }
        }

        return String.valueOf(sum);

    }

}
