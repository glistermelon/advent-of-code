package glistermelon;

import java.io.IOException;
import java.util.Arrays;

public class Day4 extends DaySolver {

    public Day4() throws IOException {
        super(4);
    }

    int[] matchList;

    public String solvePart1() {

        int sum = 0;

        String[] lines = getPuzzleInputLines();
        matchList = new int[lines.length];

        for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
            String line = lines[lineIndex].replace("  ", " ");
            String[] split = line.substring(line.indexOf(':') + 2).split(" \\| ");
            int[] nums1 = Arrays.stream(split[0].split(" ")).mapToInt(Integer::parseInt).toArray();
            int[] nums2 = Arrays.stream(split[1].split(" ")).mapToInt(Integer::parseInt).toArray();
            int points = 1;
            int matches = 0;
            for (int n : nums1) {
                if (Arrays.stream(nums2).anyMatch(m -> n == m)) {
                    points *= 2;
                    matches++;
                }
            }
            if (matches != 0) sum += points / 2;
            matchList[lineIndex] = matches;
        }

        return String.valueOf(sum);

    }

    public String solvePart2() {

        int[] cards = new int[matchList.length];
        for (int card = 0; card < cards.length; card++) {
            cards[card]++;
            for (int i = card + 1; i < cards.length && i < card + 1 + matchList[card]; i++) {
                cards[i] += cards[card];
            }
        }
        return String.valueOf(Arrays.stream(cards).sum());

    }

}
