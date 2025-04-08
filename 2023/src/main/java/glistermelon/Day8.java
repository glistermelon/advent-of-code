package glistermelon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day8 extends DaySolver {

    public Day8() throws IOException {
        super(8);
    }

    public String solvePart1() {

        String[] lines = getPuzzleInputLines();
        String pattern = lines[0];
        Node currNode = null;
        for (int i = 2; i < lines.length; i++) {
            String line = lines[i];
            Node node = new Node(
                    line.substring(0, 3),
                    line.substring(7, 10),
                    line.substring(12, 15)
            );
            if (node.name.equals("AAA")) currNode = node;
        }

        int steps = 0;
        int i = 0;
        while (!currNode.name.equals("ZZZ")) {
            if (pattern.charAt(i) == 'R') currNode = currNode.rightNode;
            else currNode = currNode.leftNode;
            if (++i == pattern.length()) i = 0;
            steps++;
        }

        return String.valueOf(steps);

    }

    public String solvePart2() {

        String[] lines = getPuzzleInputLines();
        String pattern = lines[0];
        List<Node> nodes = new ArrayList<>();
        for (int i = 2; i < lines.length; i++) {
            String line = lines[i];
            Node node = new Node(
                    line.substring(0, 3),
                    line.substring(7, 10),
                    line.substring(12, 15)
            );
            if (node.name.endsWith("A")) nodes.add(node);
        }

        long[] starts = new long[nodes.size()];
        long[] periods = new long[starts.length];

        for (int nodeIndex = 0; nodeIndex < nodes.size(); nodeIndex++) {

            Node node = nodes.get(nodeIndex);
            int patternIndex = 0;
            long steps = 0;
            while (!node.name.endsWith("Z")) {
                if (pattern.charAt(patternIndex) == 'R') node = node.rightNode;
                else node = node.leftNode;
                if (++patternIndex == pattern.length()) patternIndex = 0;
                steps++;
            }
            starts[nodeIndex] = steps;

            List<Integer> subperiods = new ArrayList<>();
            for (int repeats = 0; true; repeats++) {
                for (int i = 0; i < pattern.length(); i++) {

                    if (pattern.charAt(patternIndex) == 'R') node = node.rightNode;
                    else node = node.leftNode;
                    if (++patternIndex == pattern.length()) patternIndex = 0;

                    int p = pattern.length() * repeats + i + 1;
                    if (node.name.endsWith("Z")) subperiods.add(p);
                    else subperiods.removeAll(subperiods.stream().filter(sp -> p % sp == 0).toList());

                }
                if (node.name.endsWith("Z")) break;
            }
            periods[nodeIndex] = subperiods.stream().min(Integer::compareTo).get();

        }

        long minStart = Arrays.stream(starts).min().getAsLong();
        long period = 0;
        for (int i = 0; i < starts.length; i++) {
            if (starts[i] == minStart) period = periods[i];
            starts[i] -= minStart;
        }
        long steps = 0;

        return String.valueOf(steps);

    }

    class Node {

        private String name;
        private String leftName;
        private String rightName;
        private Node leftNode;
        private Node rightNode;


        private static final List<Node> instances = new ArrayList<>();

        public Node(String name, String left, String right) {
            this.name = name;
            this.leftName = left;
            this.rightName = right;
            for (Node node : instances) {
                if (node.leftName.equals(name)) node.leftNode = this;
                if (node.rightName.equals(name)) node.rightNode = this;
                if (node.name.equals(leftName)) leftNode = node;
                if (node.name.equals(rightName)) rightNode = node;
            }
            instances.add(this);
        }

    }

}