package upt.coproject.testbench;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    private BooleanProperty cancelled = new SimpleBooleanProperty(false);
    @Getter
    protected BooleanProperty finished = new SimpleBooleanProperty(false);

    @Getter
    @Setter
    protected DoubleProperty runningProgress = new SimpleDoubleProperty(0);

    @Getter @Setter
    protected DoubleProperty initializingProgress = new SimpleDoubleProperty(0);

    @Getter @Setter
    protected StringProperty progressStatus = new SimpleStringProperty();


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

            benchmark.getCancelled().bind(cancelled);

            benchmark.getProgressStatus().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    getProgressStatus().setValue(benchmark.getName() +" : "+ newValue);

                    System.out.println(getProgressStatus());
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

        while (!cancelled.get() && !benchmarks.isEmpty() && !Thread.interrupted())
        {
            benchmark = benchmarks.poll();
            benchmark.start();

            results.putAll(benchmark.getResults());


            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                break;
            }
        }
        System.out.println("SEQ WRITE " + results.get("SEQ_WRITE"));
        System.out.println("RND WRITE " + results.get("RND_WRITE"));

        if (!cancelled.get())
            finished.setValue(true);
    }

    public void start(Object... params)
    {
        cancelled.setValue(false);
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
        cancelled.setValue(true);
        runningProgress.setValue(0);


    }
}
