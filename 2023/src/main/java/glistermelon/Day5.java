package glistermelon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day5 extends DaySolver {

    public Day5() throws IOException {
        super(5);
    }

    public void runSharedLogic() {}

    public String solvePart1() {

        String[] lines = getPuzzleInputLines();
        long[] seeds = Arrays.stream(lines[0].substring(lines[0].indexOf(':') + 2).split(" "))
                .mapToLong(Long::parseLong).toArray();
        long lowest = Integer.MAX_VALUE;
        for (long num : seeds) {
            int i = 3;
            while (true) {
                do {
                    long[] numbers = Arrays.stream(lines[i].split(" ")).mapToLong(Long::parseLong).toArray();
                    long dst = numbers[0], src = numbers[1], len = numbers[2];
                    if (num >= src && num - src < len) {
                        num = num - src + dst;
                        while (i < lines.length && !lines[i].isBlank()) i++;
                        break;
                    }
                } while (++i < lines.length && !lines[i].isBlank());
                if (i == lines.length) break;
                i += 2;
            }
            if (num < lowest) lowest = num;
        }
        return String.valueOf(lowest);

    }

    public String solvePart2() {

        String[] lines = getPuzzleInputLines();
        long[] seeds = Arrays.stream(lines[0].substring(lines[0].indexOf(':') + 2).split(" "))
                .mapToLong(Long::parseLong).toArray();
        long lowest = Integer.MAX_VALUE;
        for (int startIndex = 0; startIndex < seeds.length; startIndex += 2) {
            List<Range> ranges = new ArrayList<>();
            ranges.add(new Range(seeds[startIndex], seeds[startIndex] + seeds[startIndex + 1]));
            int i = 3;
            while (true) {
                List<Range> newRanges = new ArrayList<>();
                do {
                    long[] numbers = Arrays.stream(lines[i].split(" ")).mapToLong(Long::parseLong).toArray();
                    long dst = numbers[0], len = numbers[2];
                    Range src = new Range(numbers[1], numbers[1] + len);
                    List<Range> removeRanges = new ArrayList<>();
                    List<Range> leftoverRanges = new ArrayList<>();
                    for (Range range : ranges) {
                        Range intersection = range.intersection(src);
                        if (intersection != null) {
                            newRanges.add(new Range(
                                    intersection.start() - src.start() + dst,
                                    intersection.end() - src.start() + dst
                            ));
                            removeRanges.add(range);
                            leftoverRanges.addAll(range.remove(intersection));
                        }
                    }
                    ranges.removeAll(removeRanges);
                    ranges.addAll(leftoverRanges);
                } while (++i < lines.length && !lines[i].isBlank());
                ranges.addAll(newRanges);
                if (i == lines.length) break;
                i += 2;
            }
            for (Range range : ranges) {
                if (range.start() < lowest) lowest = range.start();
            }
        }
        return String.valueOf(lowest);

    }

}