package glistermelon;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.util.NullOutputStream;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public abstract class DaySolver {

    private final int day;
    protected final String puzzleInput;
    protected boolean sharedLogicDone = false;

    public DaySolver(int day) {
        this.day = day;
        String puzzleInput = "";
        try {
            puzzleInput = Files.readString(Path.of("inputs/" + day + ".txt"));
        }
        catch (Exception e) {
            System.out.println("WARNING/ERROR in DaySolver constructor:" + e);
        }
        this.puzzleInput = puzzleInput;
    }

    protected String[] getPuzzleInputLines() {
        return this.puzzleInput.replace("\r", "").split("\n");
    }

    public abstract void runSharedLogic();
    public abstract String solvePart1();
    public abstract String solvePart2();

    public String solvePart1Safe() {
        if (!sharedLogicDone) {
            runSharedLogic();
            sharedLogicDone = true;
        }
        return solvePart1();
    }

    public String solvePart2Safe() {
        if (!sharedLogicDone) {
            runSharedLogic();
            sharedLogicDone = true;
        }
        return solvePart2();
    }

    public enum BenchmarkChoice {
        SharedLogic,
        Part1,
        Part2
    }

    private Options getBenchmarkOptions(BenchmarkChoice choice, boolean precise) {
        String regex = switch (choice) {
            case SharedLogic -> SharedLogicBenchmarker.class.getName() + ".runSharedLogic";
            case Part1 -> DayBenchmarker.class.getName() + ".runPart1";
            case Part2 -> DayBenchmarker.class.getName() + ".runPart2";
        };
        return new OptionsBuilder()
                .include(regex)
                .forks(1)
                .warmupIterations(1)
                .measurementIterations(1)
                .mode(precise ? Mode.AverageTime : Mode.SingleShotTime)
                .timeUnit(TimeUnit.MILLISECONDS)
                .param("dayNum", String.valueOf(day))
                .build();
    }

    public double benchmark(BenchmarkChoice choice, boolean precise) throws RunnerException {
        final Options options = getBenchmarkOptions(choice, precise);
        var out = System.out;
        System.setOut(new PrintStream(new NullOutputStream()));
        double result = new Runner(options).runSingle().getPrimaryResult().getScore();
        System.setOut(out);
        return result;
    }

    public void printOutputs(boolean preciseBenchmarks) throws RunnerException {
        System.out.println("\u001b[4m" + "Solutions" + "\u001b[24m");
        System.out.println("Part 1: \u001b[1m" + solvePart1Safe() + "\u001b[22m");
        System.out.println("Part 2: \u001b[1m" + solvePart2Safe() + "\u001b[22m");
        System.out.println();
        System.out.println("\u001b[4m" + "Benchmarks" + "\u001b[24m");
        String bench0 = Util.doubleToString(benchmark(BenchmarkChoice.SharedLogic, preciseBenchmarks), 3);
        String bench1 = Util.doubleToString(benchmark(BenchmarkChoice.Part1, preciseBenchmarks), 3);
        String bench2 = Util.doubleToString(benchmark(BenchmarkChoice.Part2, preciseBenchmarks), 3);
        System.out.println("Shared: \u001b[1m" + bench0 + " ms\u001b[22m");
        System.out.println("Part 1: \u001b[1m" + bench1 + " ms\u001b[22m");
        System.out.println("Part 2: \u001b[1m" + bench2 + " ms\u001b[22m");
    }
    public void printOutputs() throws RunnerException {
        printOutputs(false);
    }

    public static DaySolver getSolver(int day) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return (DaySolver)Class.forName("glistermelon.Day" + day).getDeclaredConstructor().newInstance();
    }

}