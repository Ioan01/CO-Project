package Benchmark;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import lombok.Getter;
import lombok.Setter;

public abstract class Benchmark {


    public Benchmark() {
        runningThread = new Thread(this::run);
    }

    private final Thread runningThread;

    @Getter @Setter
    protected DoubleProperty runningProgress = new SimpleDoubleProperty(0);

    @Getter
    protected DoubleProperty initializingProgress = new SimpleDoubleProperty(0);
    /**
     * Runs the benchmark algorithm
     */
    public abstract void run();

    /**
     * Initialize the benchmark
     * @param params any type or number of arguments
     */
    public abstract void initialize(Object ... params);
    public abstract void clean();


    public abstract void warmup();

    public void start()
    {
        runningThread.start();
    }

    public void cancel()
    {
        runningThread.interrupt();
    }
}
