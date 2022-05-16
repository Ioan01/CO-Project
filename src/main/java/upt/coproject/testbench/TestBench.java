package upt.coproject.testbench;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableMapValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableMap;
import lombok.Getter;
import lombok.Setter;
import upt.coproject.benchmark.Benchmark;

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
    protected Map<String, Object> results = new HashMap<>();
    private boolean running = true;
    @Getter
    protected BooleanProperty finished = new SimpleBooleanProperty(false);
  
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
        for (Benchmark benchmark:benchmarks)
        {
            benchmark.runningProgress.addListener(new ChangeListener<Number>()
            {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1)
                {
                    double delta = t1.doubleValue() - number.doubleValue();

                    runningProgress.setValue(runningProgress.get() + delta / initialBenchmarkCount);

                }
            });
        }
    }

    public void run(Object ... params)
    {
        // temporary list

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

                results.putAll(benchmark.getResults());
                System.out.println(results.get("SEQ_WRITE"));
                Thread.sleep(100);
            }

            finished.setValue(true);
        }
        catch (InterruptedException e)
        {
            benchmark.cancel();
            runningProgress.setValue(0);
        }

    }

    public void start(Object... params)
    {
        finished.setValue(false);
        if (runningThread.isAlive())
            return;

        runningProgress.setValue(0);

        boolean flag = false;
        try
        {
            runningThread.start();
        }
        catch (IllegalThreadStateException e)
        {
            runningThread = new Thread(()->run(params));
            flag = true;
        }

        if (flag)
            runningThread.start();
    }

    public void cancel()
    {
        running = false;



        runningThread.interrupt();
        runningProgress.setValue(0);


    }
}
