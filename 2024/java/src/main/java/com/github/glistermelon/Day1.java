package com.github.glistermelon;

import java.io.IOException;
import java.util.*;

public class Day1 extends DaySolver {

    private List<Integer> list1 = new ArrayList<Integer>();
    private List<Integer> list2 = new ArrayList<Integer>();

    public Day1() throws IOException {
        parseInput();
    }

    private void parseInput() throws IOException {
        for (String line : getInputLines(1)) {
            final Integer[] arr = Arrays.stream(line.split("   ")).map(Integer::parseInt).toArray(Integer[]::new);
            list1.add(arr[0]);
            list2.add(arr[1]);
        }
    }

    public int solvePart1() {
        Collections.sort(list1);
        Collections.sort(list2);
        int solution = 0;
        for (int i = 0; i < list1.size(); ++i) {
            solution += Math.abs(list1.get(i) - list2.get(i));
        }
        return solution;
    }

    public int solvePart2() {
        int solution = 0;
        for (int i : list1) {
            solution += i * Collections.frequency(list2, i);
        }
        return solution;
    }

}
