package bench;
//RANDOM COMMENT

public class SleepDemoBenchmark implements IBenchmark{
    private int n;
    private boolean running;
    public void run()
    {
        try
        {
            Thread.sleep(n);
        }catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    public void run(Object ... params)
    {

    }
    public void initialize(Object ... params)
    {
        this.n = (Integer)params[0];
    }
    public void clean()
    {

    }
    public void display()
    {

    }
    public void cancel()
    {

    }
}
