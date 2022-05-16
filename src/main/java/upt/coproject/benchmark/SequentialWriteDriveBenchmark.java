package upt.coproject.benchmark;

import upt.coproject.timing.Timer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
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

    private double write(String file) throws IOException {
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

        byte[] bytes = new byte[(int) bufferSizeEnd];
        rng.nextBytes(bytes);
        timer.start();
        for (int bufferSize = (int) bufferSizeStart; bufferSize <= bufferSizeEnd; bufferSize = bufferSize*4) {
            File file1 = new File(drive + tempFileLocation+'/' + file);

            if (file1.exists())
                file1.createNewFile();

            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file1.getAbsolutePath()),bufferSize);


            //rng.nextBytes(bytes);
            long writtenBytes = 0;
            while (writtenBytes < fileSize)
            {
                outputStream.write(bytes,0,bufferSize);
                writtenBytes+=bytes.length;
            }



            outputStream.close();
            totalWritten+=file1.length();
            file1.deleteOnExit();
        }


        long elapsed = timer.stop();

        return (totalWritten / 1024 / 1024) / (elapsed / Math.pow(10,9));



    }

    @Override
    public void run()
    {
        int iterations = 5;

        ArrayList<Double> writeSpeeds  = new ArrayList<Double>();

        try
        {
            for (int i=0;i<iterations;i++)
            {
                writeSpeeds.add(write("output"+i+".txt"));

                runningProgress.setValue(runningProgress.get() + 0.5/iterations);
            }

            double avg = writeSpeeds.stream().reduce(0.0, Double::sum) / writeSpeeds.size();
            results.put("SEQ_WRITE",avg);


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
        int iterations = 10;
        try
        {
            runningProgress.setValue(0);
            fileSize = fileSize/8;
            for (int i=0;i<iterations;i++)
            {
                write("warmup"+i+".txt");
                runningProgress.setValue(runningProgress.get() + 0.5/iterations);
            }

            fileSize = fileSize*8;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }
}
