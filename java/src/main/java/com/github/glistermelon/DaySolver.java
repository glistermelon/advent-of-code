package com.github.glistermelon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public abstract class DaySolver {

    public static String getInputFilePath(int dayNumber) {
        return "inputs/day" + String.valueOf(dayNumber) + ".txt";
    }

    public static List<String> getInputLines(int dayNumber) throws IOException {
        return new BufferedReader(new InputStreamReader(
                Main.class.getResourceAsStream(getInputFilePath(dayNumber))
        )).lines().toList();
    }

    public static String getInput(int dayNumber) throws IOException {
        return new BufferedReader(new InputStreamReader(
                Main.class.getResourceAsStream(getInputFilePath(dayNumber))
        )).lines().collect(Collectors.joining("\n"));
    }

    public abstract int solvePart1() throws IOException;
    public abstract int solvePart2() throws IOException;
}