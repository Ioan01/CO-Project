package upt.coproject.benchmark;

import upt.coproject.timing.Timer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class SequentialWriteDriveBenchmark extends Benchmark
{
    private String drive;
    private final int[] bufferSizes = {4*1024,64*1024,256*1024};

    private long fileSize;



    public SequentialWriteDriveBenchmark()
    {
        super("Sequential Write");
    }

    public void initialize(String drive,long fileSize)
    {
        this.drive = drive;


        this.fileSize = fileSize;
    }

    private double write(String file) throws IOException {
        Random rng = new Random();
        String tempFileLocation = "benchmark/temp" + rng.nextInt();
        File outputFolder = new File(drive + tempFileLocation);
        if (!outputFolder.isDirectory())
        {
            outputFolder.mkdirs();
        }




        Timer timer = new Timer();

        double totalWritten = 0;



        byte[] bytes = new byte[(int) bufferSizes[bufferSizes.length-1]];

        timer.start();
        for (int bufferSize:bufferSizes) {
            rng.nextBytes(bytes);


            File file1 = new File(drive + tempFileLocation +'/' + file);

            if (file1.exists())
                file1.createNewFile();

            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file1.getAbsolutePath()),bufferSize);


            //rng.nextBytes(bytes);
            long writtenBytes = 0;
            while (writtenBytes < fileSize && !getCancelled().get())
            {
                outputStream.write(bytes,0,bufferSize);
                writtenBytes+=bytes.length;
            }



            outputStream.close();
            totalWritten+=file1.length();
            file1.deleteOnExit();

            if (getCancelled().get())
                return 0;
        }


        long elapsed = timer.stop();

        return (totalWritten / 1024 / 1024) / (elapsed / Math.pow(10,9));
    }

    @Override
    public void run()
    {


        int iterations = (int) (((512.0*1024*1024)/fileSize))*5;

        ArrayList<Double> writeSpeeds  = new ArrayList<>();

        try
        {
            for (int i=0;i<iterations&&!getCancelled().get();i++)
            {
                writeSpeeds.add(write("output"+i+".dat"));

                runningProgress.setValue(runningProgress.get() + 0.5/iterations);
            }

            double avg = writeSpeeds.stream().reduce(0.0, Double::sum) / writeSpeeds.size();
            results.put("SEQ_WRITE",avg);
                    Files.walk(new File(drive+"benchmark").toPath())
                            .sorted(Comparator.reverseOrder())
                            .map(Path::toFile)
                            .forEach(File::delete);


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
        int iterations = (int) (((512.0*1024*1024)/fileSize))*20;
        try
        {
            runningProgress.setValue(0);
            fileSize = fileSize/8;
            for (int i=0;i<iterations&&!getCancelled().get();i++)
            {
                write("warmup"+i+".dat");
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
