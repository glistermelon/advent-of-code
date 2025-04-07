package glistermelon;

import java.io.IOException;
import java.util.*;

public class Day7 extends DaySolver {

    public Day7() throws IOException {
        super(7);
    }

    private String solve(boolean allowJokers) {

        List<PokerHand> hands = new ArrayList<>();
        for (String line : getPuzzleInputLines()) {
            String[] split = line.split(" ");
            hands.add(new PokerHand(split[0], Long.parseLong(split[1]), allowJokers));
        }
        hands.sort(new PokerHandComparator(allowJokers));

        long sum = 0;
        for (int i = 0; i < hands.size(); i++) {
            sum += (i + 1) * hands.get(i).bid();
        }
        return String.valueOf(sum);

    }

    public String solvePart1() {
        return solve(false);
    }

    public String solvePart2() {
        return solve(true);
    }

    enum HandType {
        HighCard,
        OnePair,
        TwoPair,
        ThreeKind,
        FullHouse,
        FourKind,
        FiveKind
    }

    record PokerHand(String data, HandType handType, long bid) {

        public PokerHand(String data, long bid, boolean allowJokers) {
            this(data, analyzeHand(data, allowJokers), bid);
        }

        static HandType analyzeHand(String data, boolean allowJokers) {

            int jokers = 0;
            if (allowJokers) {
                int prevLen = data.length();
                data = data.replace("J", "");
                jokers = prevLen - data.length();
                if (jokers == 5) return HandType.FiveKind;
            }

            Map<Character, Integer> groups = new HashMap<>();
            for (char c : data.toCharArray()) {
                groups.put(c, groups.getOrDefault(c, 0) + 1);
            }
            Character[] keys = groups.keySet().stream().sorted(Comparator.comparingInt(groups::get).reversed()).toArray(Character[]::new);
            int[] values = Arrays.stream(keys).mapToInt(groups::get).toArray();
            values[0] += jokers;
            if (values[0] == 5) return HandType.FiveKind;
            if (values[0] == 4) return HandType.FourKind;
            if (values[0] == 3) {
                return values[1] == 2 ? HandType.FullHouse : HandType.ThreeKind;
            }
            if (values[0] == 2) {
                return values[1] == 2 ? HandType.TwoPair : HandType.OnePair;
            }
            return HandType.HighCard;

        }

        static int getCardRank(char c, boolean allowJokers) {
            if (allowJokers && c == 'J') return 1;
            return Character.isDigit(c) ? c - '0' : "TJQKA".indexOf(c) + 10;
        }

    }

    static class PokerHandComparator implements Comparator<PokerHand> {

        boolean allowJokers;

        public PokerHandComparator(boolean allowJokers) {
            this.allowJokers = allowJokers;
        }

        public int compare(PokerHand h1, PokerHand h2) {
            var comp = h1.handType().compareTo(h2.handType());
            if (comp != 0) return comp;
            String data1 = h1.data(), data2 = h2.data();
            for (int i = 0; i < data1.length(); i++) {
                comp = Integer.compare(
                        PokerHand.getCardRank(data1.charAt(i), allowJokers),
                        PokerHand.getCardRank(data2.charAt(i), allowJokers)
                );
                if (comp != 0) return comp;
            }
            return 0;
        }

    }

}