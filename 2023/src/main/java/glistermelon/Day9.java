package glistermelon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day9 extends DaySolver {

    public Day9() throws IOException {
        super(9);
    }

    public String solvePart1() {

        int sum = 0;
        for (String input : getPuzzleInputLines()) {
            List<Integer> numbers = new ArrayList<>(Arrays.stream(input.split(" "))
                    .mapToInt(Integer::parseInt).boxed().toList());
            List<Integer> trails = new ArrayList<>();
            while (numbers.getFirst() != numbers.getLast()) {
                trails.add(numbers.getLast());
                for (int i = numbers.size() - 1; i > 0; i--) {
                    numbers.set(i, numbers.get(i) - numbers.get(i - 1));
                }
                numbers.removeFirst();
            }
            int n = numbers.getFirst() + trails.stream().mapToInt(Integer::intValue).sum();
            sum += n;
        }
        return String.valueOf(sum);

    }

    public String solvePart2() {

        int sum = 0;
        for (String input : getPuzzleInputLines()) {
            List<Integer> numbers = new ArrayList<>(Arrays.stream(input.split(" "))
                    .mapToInt(Integer::parseInt).boxed().toList());
            List<Integer> trails = new ArrayList<>();
            int steps;
            for (steps = 0; numbers.getFirst() != numbers.getLast(); steps++) {
                trails.add(numbers.getFirst());
                for (int i = numbers.size() - 1; i > 0; i--) {
                    numbers.set(i, numbers.get(i) - numbers.get(i - 1));
                }
                numbers.removeFirst();
            }
            int extrapolated = numbers.getFirst();
            while (!trails.isEmpty()) extrapolated = trails.removeLast() - extrapolated;
            sum += extrapolated;
        }
        return String.valueOf(sum);

    }

}