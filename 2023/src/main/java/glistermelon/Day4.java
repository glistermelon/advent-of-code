package glistermelon;

import java.io.IOException;
import java.util.Arrays;

public class Day4 extends DaySolver {

    public Day4() throws IOException {
        super(4);
    }

    int[] matchList;

    public void runSharedLogic() {

        String[] lines = getPuzzleInputLines();
        matchList = new int[lines.length];

        for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
            String line = lines[lineIndex].replace("  ", " ");
            String[] split = line.substring(line.indexOf(':') + 2).split(" \\| ");
            int[] nums1 = Arrays.stream(split[0].split(" ")).mapToInt(Integer::parseInt).toArray();
            int[] nums2 = Arrays.stream(split[1].split(" ")).mapToInt(Integer::parseInt).toArray();
            int matches = 0;
            for (int n : nums1) {
                if (Arrays.stream(nums2).anyMatch(m -> n == m)) matches++;
            }
            matchList[lineIndex] = matches;
        }

    }

    public String solvePart1() {

        return String.valueOf(
                Arrays.stream(matchList).map(n -> {
                    int p = 1;
                    for (int i = 0; i < n; i++) p *= 2;
                    p /= 2;
                    return p;
                }).sum()
        );

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
