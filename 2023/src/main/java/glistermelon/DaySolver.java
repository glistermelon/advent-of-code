package glistermelon;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class DaySolver {

    private final int day;
    private final String puzzleInput;

    public DaySolver(int day) throws IOException {
        this.day = day;
        this.puzzleInput = Files.readString(Path.of("inputs/" + day + ".txt"));
    }

    protected String[] getPuzzleInputLines() {
        return this.puzzleInput.replace("\r", "").split("\n");
    }

    public abstract String solvePart1();
    public abstract String solvePart2();

    public BenchmarkData benchmark(int part) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        long start = System.nanoTime();
        String sol = (String)this.getClass().getMethod("solvePart" + part).invoke(this);
        double time = (double)(System.nanoTime() - start) / 1000000;
        return new BenchmarkData(sol, time);
    }

    public BenchmarkData benchmarkPart1() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return benchmark(1);
    }

    public BenchmarkData benchmarkPart2() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return benchmark(2);
    }

    public void printOutputs() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        System.out.println(benchmarkPart1());
        System.out.println(benchmarkPart2());
    }

    public static DaySolver getSolver(int day) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return (DaySolver)Class.forName("glistermelon.Day" + day).getDeclaredConstructor().newInstance();
    }

}