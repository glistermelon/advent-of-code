package com.github.glistermelon;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Day2 extends DaySolver {

    private List<List<Integer>> reports = new ArrayList<>();

    public Day2() throws IOException {
        parseInput();
    }

    private void parseInput() throws IOException {
        for (String line : getInputLines(2))
            reports.add(new ArrayList<>(Arrays.stream(line.split(" ")).map(Integer::parseInt).toList()));
    }

    private boolean safetyCheck(List<Integer> report, AtomicInteger badIndex, AtomicBoolean signViolation) {
        List<Integer> deltas = IntStream.range(1, report.size())
                .mapToObj(i -> report.get(i) - report.get(i - 1))
                .toList();
        for (int i = 0; i < deltas.size(); i++) {
            int delta = Math.abs(deltas.get(i));
            if (delta == 0 || delta > 3) {
                if (badIndex != null) badIndex.set(i);
                if (signViolation != null) signViolation.set(false);
                return false;
            }
        }
        boolean expectPositive = deltas.get(0) > 0;
        for (int i = 0; i < deltas.size(); i++) {
            int delta = deltas.get(i);
            if ((expectPositive && delta < 0) || (!expectPositive && delta > 0)) {
                if (badIndex != null) badIndex.set(i);
                if (signViolation != null) signViolation.set(true);
                return false;
            }
        }
        return true;
    }

    private boolean safeWithOneRemoved(List<Integer> report, int index) {
        int n = report.remove(index);
        if (safetyCheck(report, null, null)) return true;
        report.add(index, n);
        n = report.remove(index + 1);
        if (safetyCheck(report, null, null)) return true;
        report.add(index + 1, n);
        return false;
    }

    public int solvePart1() {
        int solution = 0;
        for (var report : reports) {
            if (safetyCheck(report, null, null)) solution++;
        }
        return solution;
    }

    public int solvePart2() {
        int solution = 0;
        for (var report : reports) {

            var i = new AtomicInteger();
            var signViolation = new AtomicBoolean();

            if (
                safetyCheck(report, i, signViolation)
                || safeWithOneRemoved(report, i.get())
                || (signViolation.get() && safeWithOneRemoved(report, 0))
            )
            {
                solution++;
                continue;
            }

        }
        return solution;
    }

}
