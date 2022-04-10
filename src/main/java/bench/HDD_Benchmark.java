package bench;

public class HDD_Benchmark extends Benchmark {
    String fixed_size;

    public void streamWriteFixedSize()
    {

    }

    public void run()
    {
        System.out.println("Starting HDD Benchmark...");
        long end = System.currentTimeMillis() +  10000;
        System.out.println("Testing HDD . . .");
        while(System.currentTimeMillis() < end) {
            // do something
            // pause to avoid churning
            System.out.println(". . .");
        }
        System.out.println("Done!");
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
