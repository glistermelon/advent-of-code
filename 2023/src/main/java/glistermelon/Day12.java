package glistermelon;

import java.io.IOException;
import java.util.*;

public class Day12 extends DaySolver {

    public Day12() throws IOException {
        super(12);
    }

    Map<String, Map<List<Integer>, Long>> cache = new HashMap<>();

    public String solvePart1() {
        return solve(1);
    }

    public String solvePart2() {
        return solve(5);
    }

    private String solve(int repeats) {

        long sum = 0;
        for (String line : getPuzzleInputLines()) {
            List<Integer> lengths = new ArrayList<>();
            String input = parseLine(line, lengths, repeats);
            sum += combinations(input, lengths);
        }
        return String.valueOf(sum);

    }

    private String parseLine(String line, List<Integer> output, int repeats) {

        String[] split = line.split(" ");
        String input = split[0];
        input = String.join("?", Collections.nCopies(repeats, input));
        while (input.endsWith(".")) input = input.substring(0, input.length() - 1);
        input = input + ".";
        List<Integer> lengths = new ArrayList<>(
                Arrays.stream(split[1].split(","))
                        .map(Integer::parseInt).toList()
        );
        for (int i = 0; i < repeats; i++) output.addAll(lengths);
        return input;

    }

    private long combinations(String input, List<Integer> lengths) {

        var cached1 = cache.get(input);
        if (cached1 == null) cache.put(input, new HashMap<>());
        else {
            Long cached2 = cached1.get(lengths);
            if (cached2 != null) return cached2;
        }

        if (lengths.isEmpty()) return input.contains("#") ? 0 : 1;
        int length = lengths.removeFirst();
        long combos = 0;

        int cutoff = input.length() - length;
        if (input.contains("#")) cutoff = Integer.min(cutoff, input.indexOf("#") + 1);
        for (int i = 0; i < cutoff; i++) {
            String s = input.substring(i, i + length);
            if (s.contains(".")) continue;
            int j = i + s.length();
            if (input.charAt(j) == '#') continue;
            combos += combinations(input.substring(j + 1), lengths);
        }
        lengths.addFirst(length);

        cache.get(input).put(lengths, combos);

        return combos;

    }

}
