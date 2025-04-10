package glistermelon;

import org.openjdk.jmh.runner.RunnerException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, RunnerException {
        DaySolver.getSolver(10).printOutputs();
    }
}