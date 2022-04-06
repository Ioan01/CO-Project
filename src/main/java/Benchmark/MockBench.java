package Benchmark;

public class MockBench extends Benchmark{

    @Override
    public void run() {
        for (int i =0;i<100;i++)
        {
            try {
                Thread.sleep(10);
                runningProgress.setValue(runningProgress.getValue()+1);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    @Override
    public void initialize(Object... params) {

    }

    @Override
    public void clean() {

    }

    @Override
    public void warmup() {

    }
}
