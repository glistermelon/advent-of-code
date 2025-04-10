package glistermelon;

import java.io.IOException;
import java.util.Arrays;

public class Day6 extends DaySolver {

    public Day6() throws IOException {
        super(6);
    }

    public void runSharedLogic() {}

    public String solvePart1() {

        String[] input = Arrays.stream(getPuzzleInputLines())
                .map(s -> s.replaceAll(" +", " "))
                .map(s -> s.substring(s.indexOf(':') + 2)).toArray(String[]::new);
        long[] times = Arrays.stream(input[0].split(" ")).mapToLong(Long::parseLong).toArray();
        long[] distances = Arrays.stream(input[1].split(" ")).mapToLong(Long::parseLong).toArray();

        long product = 1;
        for (int round = 0; round < times.length; round++) {
            var time = times[round];
            var dist = distances[round];
            long ways = 0;
            for (int i = 1; i < time; i++) {
                if (i * (time - i) > dist) ways++;
            }
            product *= ways;
        }

        return String.valueOf(product);

    }

    public String solvePart2() {

        long[] input = Arrays.stream(getPuzzleInputLines())
                .map(s -> s.replaceAll(" ", ""))
                .map(s -> s.substring(s.indexOf(':') + 1))
                .mapToLong(Long::parseLong).toArray();
        var t = input[0];
        var d = input[1];

        // (T - i)i > D
        // i^2 - Ti + D < 0
        // root1 = (T - sqrt(T^2 - 4D)) / 2
        // root2 = (T + sqrt(T^2 - 4D)) / 2

        long root1 = (long)Math.ceil((t - Math.sqrt((double)(t * t - 4 * d))) / 2);
        long root2 = (long)Math.floor((t + Math.sqrt((double)(t * t - 4 * d))) / 2);
        return String.valueOf(root2 - root1 + 1);

    }

}