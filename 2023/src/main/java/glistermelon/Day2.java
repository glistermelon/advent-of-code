package glistermelon;

import java.io.IOException;

public class Day2 extends DaySolver {

    public Day2() throws IOException {
        super(2);
    }

    private final int maxRed = 12;
    private final int maxGreen = 13;
    private final int maxBlue = 14;

    public String solvePart1() {
        int sum = 0, gameId = 0;
        for (String ln : getPuzzleInputLines()) {
            gameId++;
            ln = ln.substring(ln.indexOf(':') + 2);
            ln = ln.replace(";", ",").replace(", ", ",");
            boolean possible = true;
            for (String datum : ln.split(",")) {
                int i = datum.indexOf(' ');
                int count = Integer.parseInt(datum.substring(0, i));
                char color = datum.charAt(i + 1);
                if (
                        (color == 'r' && count > maxRed) ||
                        (color == 'g' && count > maxGreen) ||
                        (color == 'b' && count > maxBlue)
                ) {
                    possible = false;
                    break;
                }
            }
            if (possible) sum += gameId;
        }
        return String.valueOf(sum);
    }

    public String solvePart2() {
        int sum = 0, gameId = 0;
        for (String ln : getPuzzleInputLines()) {
            gameId++;
            ln = ln.substring(ln.indexOf(':') + 2);
            ln = ln.replace(";", ",").replace(", ", ",");
            int r = 0, g = 0, b = 0;
            for (String datum : ln.split(",")) {
                int i = datum.indexOf(' ');
                int count = Integer.parseInt(datum.substring(0, i));
                char color = datum.charAt(i + 1);
                switch (color) {
                    case 'r':
                        if (count > r) r = count;
                        break;
                    case 'g':
                        if (count > g) g = count;
                        break;
                    case 'b':
                        if (count > b) b = count;
                        break;
                }
            }
            sum += r * g * b;
        }
        return String.valueOf(sum);
    }

}
