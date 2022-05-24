package upt.coproject.benchmark;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public abstract class Benchmark {
    /**
     * How many times each individual benchmark will run and add
     * results to be used in scoring
     */
    protected static int benchmarkRepeats = 10;


    @Getter
    @Setter
    private String name;

    @Getter
    /**
     * This is where the individual benchmark will store the results gotten from running it
     */
    protected Map<String,Object> results = new HashMap<>();

    @Getter
    private BooleanProperty cancelled = new SimpleBooleanProperty();

    public Benchmark(String name) {
    }


    @Getter @Setter
    public DoubleProperty runningProgress = new SimpleDoubleProperty(0);

    /**
     * Runs the benchmark algorithm
     */
    public abstract void run();


    public abstract void clean();


    public abstract void warmup();

    public void start()
    {
        run();
    }

    public void cancel()
    {


    }

    public void join() throws InterruptedException
    {
    }
}
