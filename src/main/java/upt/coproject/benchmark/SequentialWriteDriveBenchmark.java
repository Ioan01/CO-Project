package upt.coproject.benchmark;

import upt.coproject.timing.Timer;

import java.io.*;
import java.util.Random;

public class SequentialWriteDriveBenchmark extends Benchmark
{
    private String drive;
    private long bufferSizeStart;
    private long bufferSizeEnd;

    private static String tempFileLocation = "benchmark/temp";
    private long fileSize;


    public SequentialWriteDriveBenchmark()
    {
        super("Sequential Write");
    }

    public void initialize(String drive,long bufferSizeStart,long bufferSizeEnd,long fileSize)
    {
        this.drive = drive;

        this.bufferSizeStart = bufferSizeStart;
        this.bufferSizeEnd = bufferSizeEnd;
        this.fileSize = fileSize;
    }

    private void write(String file) throws IOException {
        File outputFolder = new File(drive + tempFileLocation);
        if (!outputFolder.isDirectory())
        {
            outputFolder.mkdirs();
        }

        Random rng = new Random();


        Timer timer = new Timer();



        double totalWritten = 0;

        int ratio = (int) (bufferSizeEnd/ bufferSizeStart);
        int iterations = 0;
        while (ratio != 1)
        {
            ratio = ratio/4;
            iterations++;
        }





        timer.start();
        char[] bytes = new char[(int) bufferSizeEnd];
        for (int bufferSize = (int) bufferSizeStart; bufferSize <= bufferSizeEnd; bufferSize = bufferSize*4) {
            File file1 = new File(drive + tempFileLocation+'/' + file );

            if (file1.exists())
                file1.createNewFile();

            FileWriter fileWriter = new FileWriter(file1);


            //rng.nextBytes(bytes);
            long writtenBytes = 0;
            while (writtenBytes < fileSize)
            {
                fileWriter.write(bytes,0,bufferSize);
                writtenBytes+=bytes.length;
            }



            fileWriter.close();
            totalWritten+=file1.length();
            file1.delete();
            runningProgress.setValue(runningProgress.get() + 0.5/iterations);
        }


        long elapsed = timer.stop();

        System.out.println("Speed : " + (totalWritten / 1024 / 1024) / (elapsed / Math.pow(10,9)) + " MB/s");



    }

    @Override
    public void run()
    {
        try
        {
            write("output.txt");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    @Override
    public void clean()
    {

    }

    @Override
    public void warmup()
    {
        try
        {
            fileSize = fileSize/4;
            write("warmup.txt");
            fileSize = fileSize*4;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }
}
