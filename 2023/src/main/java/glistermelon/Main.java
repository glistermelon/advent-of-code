package glistermelon;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.FileSystems;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DaySolver.getSolver(1).printOutputs();
    }
}