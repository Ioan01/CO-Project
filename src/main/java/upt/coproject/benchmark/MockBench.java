package upt.coproject.benchmark;

public class MockBench extends Benchmark{
    private double secondsToSleep;

    public MockBench(String name)
    {
        super(name);
    }



    @Override
    public void run() {
        runningProgress.setValue(0);


        int progressIncrements = 100;

        for (int j=0;j<benchmarkRepeats;j++)
        {
            for (int i =0;i<progressIncrements;i++)
            {
                try {
                    Thread.sleep((long) (secondsToSleep/progressIncrements * 1000L));
                    runningProgress.setValue(runningProgress.getValue()+1.0/progressIncrements/benchmarkRepeats);
                    //System.out.println(runningProgress.getValue());



                } catch (InterruptedException e) {
                    break;
                }
            }


            // save results each time
        }

    }



    public void initialize(double secondsToSleep) {
        this.secondsToSleep = secondsToSleep;
    }

    @Override
    public void clean() {

    }

    @Override
    public void warmup() {

    }
}
