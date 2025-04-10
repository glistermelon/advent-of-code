package glistermelon;

import org.openjdk.jmh.annotations.*;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class SharedLogicBenchmarker {

    @Param({})
    public String dayNum;
    private DaySolver solver = null;

    public void setDay(DaySolver solver) {
        this.solver = solver;
    }

    @Benchmark
    public void runSharedLogic() {
        solver.runSharedLogic();
    }

    @Setup(Level.Invocation)
    public void setUp() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        solver = DaySolver.getSolver(Integer.parseInt(dayNum));
    }

}