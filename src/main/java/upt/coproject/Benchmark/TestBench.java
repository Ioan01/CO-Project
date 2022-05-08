package upt.coproject.Benchmark;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public abstract class TestBench
{
    private Thread runningThread;

    protected Queue<Benchmark> benchmarks = new LinkedList<>();

    protected int initialBenchmarkCount;

    /**
     * This dictionary contains the result dictionary for all benchmarks in the test bench
     * To access the results from one benchmark, use the name key
     */
    @Getter
    protected Map<String, Map<String,Object>> results = new HashMap<>();

    private boolean running = true;



    @Getter
    @Setter
    protected DoubleProperty runningProgress = new SimpleDoubleProperty(0);

    @Getter @Setter
    protected DoubleProperty initializingProgress = new SimpleDoubleProperty(0);

    protected TestBench(Object... params)
    {
        runningThread = new Thread(()->run(params));
    }

    protected void trackRunningProgress()
    {
        for (Benchmark benchmark:benchmarks) {
            benchmark.runningProgress.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    double delta = t1.doubleValue() - number.doubleValue();

                    runningProgress.setValue(runningProgress.get() + delta / initialBenchmarkCount);

                }
            });
        }
    }

    public abstract void clean();



    public void run(Object ... params)
    {
        Benchmark benchmark = null;

        results.clear();


        initialBenchmarkCount = benchmarks.size();

        for (Benchmark benchmark1: benchmarks)
        {
            benchmark1.warmup();
        }


        try
        {
            while (running && !benchmarks.isEmpty())
            {
                benchmark = benchmarks.poll();

                benchmark.start();

                benchmark.join();

                results.put(benchmark.getName(),benchmark.getResults());
            }
            clean();
        }
        catch (InterruptedException e)
        {
            benchmark.cancel();
        }

    }

    public void start(Object... params)
    {
        runningProgress.setValue(0);




        try
        {
            runningThread.start();
        }
        catch (IllegalThreadStateException e)
        {
            runningThread = new Thread(()->run(params));
            runningThread.start();
        }

    }

    public void cancel()
    {
        running = false;
        runningThread.interrupt();
    }



}
