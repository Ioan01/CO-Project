package upt.coproject.benchmark;

public class SequentialWriteDriveBenchmark extends Benchmark
{
    private String drive;

    private static String tempFileLocation = "";
    private int fileSizeMB;


    public SequentialWriteDriveBenchmark()
    {
        super("Sequential Write");
    }

    public void initialize(String drive,int fileSizeMB)
    {
        this.drive = drive;

        this.fileSizeMB = fileSizeMB;
    }

    @Override
    public void run()
    {

    }

    @Override
    public void clean()
    {

    }

    @Override
    public void warmup()
    {
        for ()
    }
}
