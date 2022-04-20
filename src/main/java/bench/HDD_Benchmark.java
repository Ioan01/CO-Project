package bench;

public class HDD_Benchmark extends Benchmark {

    String fixed_size;
    public double progress = 0.0;


    public void streamWriteFixedSize()
    {

    }

    public void run()
    {
        for (int i = 1; i <= 100; i++){
            try {
                this.progress += i;
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(i);
            }
    }

    public double getProgress()
    {
        return this.progress;
    }
    /**
     * First parameter should specify the size of the files to be written
     * Second parameter is a boolean -> should specify if the files should be deleted at the end
     */
    public void run(Object ... params)
    {
        this.fixed_size = (String) params[0];

    }

    public void initialize(Object ... params)
    {

    }
    public void display()
    {

    }
    public void clean()
    {

    }
    public void cancel()
    {

    }
    public void warmup()
    {

    }
}
