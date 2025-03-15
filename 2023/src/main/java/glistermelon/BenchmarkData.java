package glistermelon;

public record BenchmarkData(String solution, double runtime) {

    public String toString() {
        return  solution + " | " + runtime + " ms";
    }

}