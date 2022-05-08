package upt.coproject.Benchmark;

public class MockBench extends Benchmark{
    private double secondsToSleep;

    public MockBench(String name)
    {
        super(name);
    }



    @Override
    public void run() {
        runningProgress.setValue(0);

        for (int i =0;i<100;i++)
        {
            try {
                Thread.sleep((long) (secondsToSleep* 1000L));
                runningProgress.setValue(runningProgress.getValue()+0.01);
                System.out.println(runningProgress.getValue());



            } catch (InterruptedException e) {
                break;
            }
        }
    }



    public void initialize(double secondsToSleep) {
        this.secondsToSleep = secondsToSleep/100;
    }

    @Override
    public void clean() {

    }

    @Override
    public void warmup() {

    }
}
