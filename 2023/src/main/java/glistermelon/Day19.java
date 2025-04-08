package glistermelon;

import java.io.IOException;
import java.util.*;

public class Day19 extends DaySolver {

    public Day19() throws IOException {
        super(19);
    }

    List<Workflow> workflows = new ArrayList<>();
    List<Part> parts = new ArrayList<>();

    public String solvePart1() throws Exception {

        if (workflows.isEmpty()) parseInput();

        long sum = 0;
        Workflow in = Workflow.getWorkflow("in");
        for (Part part : parts) {
            if (in.handlePart(part)) sum += part.ratingSum();
        }
        return String.valueOf(sum);

    }

    public String solvePart2() {

        if (workflows.isEmpty()) parseInput();
        Workflow in = Workflow.getWorkflow("in");
        long output = in.combinations(new RangeSet[] {
                new RangeSet(new Range(1, 4001)),
                new RangeSet(new Range(1, 4001)),
                new RangeSet(new Range(1, 4001)),
                new RangeSet(new Range(1, 4001))
        });
        return String.valueOf(output);

    }

    private void parseInput() {

        boolean parsingWorkflows = true;
        for (String line : getPuzzleInputLines()) {
            if (line.isBlank()) parsingWorkflows = false;
            else if (parsingWorkflows) workflows.add(new Workflow(line));
            else parts.add(new Part(line));
        }

    }

    static class Part {

        Map<Character, Long> attributes = new HashMap<>();

        public Part(String parseMe) {
            for (String attrStr : parseMe.substring(1, parseMe.length() - 1).split(",")) {
                String[] split = attrStr.split("=");
                attributes.put(split[0].charAt(0), Long.parseLong(split[1]));
            }
        }

        public long ratingSum() {
            return attributes.values().stream().mapToLong(Long::longValue).sum();
        }

        public long getAttribute(char c) {
            return attributes.get(c);
        }

    }

    static class Workflow {

        final private Check[] checks;

        private static final Map<String, Workflow> instances = new HashMap<>();

        public Workflow(String parseMe) {
            int firstBracketIndex = parseMe.indexOf('{');
            String name = parseMe.substring(0, firstBracketIndex);
            checks = Arrays.stream(parseMe.substring(firstBracketIndex + 1, parseMe.length() - 1).split(","))
                        .map(Check::new).toArray(Check[]::new);
            instances.put(name, this);
        }

        public boolean handlePart(Part part) throws Exception {
            for (Check check : checks) {
                if (check.partPasses(part)) {
                    String dst = check.getDestination();
                    return dst == null ? check.doesAccept() : instances.get(dst).handlePart(part);
                }
            }
            throw new Exception("how did we get here");
        }

        public static Workflow getWorkflow(String name) {
            return instances.get(name);
        }

        private static long rangeSetCombinations(RangeSet[] ranges) {
            long prod = 1;
            for (RangeSet set : ranges) prod *= set.size();
            return prod;
        }

        public long combinations(RangeSet[] inputRanges) {
            long sum = 0;
            for (Check check : checks) {
                RangeSet[] newRanges = Arrays.copyOf(inputRanges, inputRanges.length);
                if (check.getCheckedAttribute() != null) {
                    int attrIndex = "xmas".indexOf(check.getCheckedAttribute());
                    long constant = check.getConstant();
                    Range range;
                    if (check.checksForGreater()) range = new Range(constant + 1, 4001);
                    else range = new Range(1, constant);
                    newRanges[attrIndex] = newRanges[attrIndex].intersection(range);
                    inputRanges[attrIndex] = inputRanges[attrIndex].remove(range);
                }
                String dst = check.getDestination();
                if (dst != null) sum += getWorkflow(dst).combinations(newRanges);
                else if (check.doesAccept()) sum += rangeSetCombinations(newRanges);
            }
            return sum;
        }

        static class Check {

            private final Character attr;
            private final boolean greater;
            private final long constant;
            private final String destination;
            private final boolean accept;

            public Check(String parseMe) {
                String result;
                if (parseMe.indexOf(':') == -1) {
                    attr = null;
                    greater = false;
                    constant = 0;
                    result = parseMe;
                }
                else {
                    String[] split = parseMe.split(":");
                    attr = split[0].charAt(0);
                    greater = split[0].charAt(1) == '>';
                    constant = Long.parseLong(split[0].substring(2));
                    result = split[1];
                }
                if (result.equals("A")) {
                    accept = true;
                    destination = null;
                }
                else if (result.equals("R")) {
                    accept = false;
                    destination = null;
                }
                else {
                    accept = false;
                    destination = result;
                }
            }

            public boolean partPasses(Part part) {
                if (attr == null) return true;
                long value = part.getAttribute(attr);
                return greater ? value > constant : value < constant;
            }

            public Character getCheckedAttribute() {
                return attr;
            }

            public long getConstant() {
                return constant;
            }

            public boolean checksForGreater() {
                return greater;
            }

            public String getDestination() {
                return destination;
            }

            public boolean doesAccept() {
                return accept;
            }

        }

    }

}