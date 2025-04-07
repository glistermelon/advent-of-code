package glistermelon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        return "";
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