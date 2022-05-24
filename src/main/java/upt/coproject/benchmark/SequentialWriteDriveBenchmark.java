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
    private final int[] bufferSizes = {64*1024,256*1024,1024*1024};

    private long fileSize;

    private static int runIterations = 5;
    private static int maxRunIterations = 20;
    private static int warmupIterations = 20;
    private static int maxWarmupIterations = 80;
    private static double idealFileSize = 512.0 * 1024*1024;



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
        String tempFileLocation = "benchmark/temp/rnd" + rng.nextInt();
        File outputFolder = new File(drive + tempFileLocation);
        if (!outputFolder.isDirectory())
        {
            outputFolder.mkdirs();
        }




        Timer timer = new Timer();

        long totalWritten = 0;



        byte[] bytes = new byte[(int) bufferSizes[bufferSizes.length-1]];

        timer.start();
        for (int bufferSize:bufferSizes) {
            rng.nextBytes(bytes);


            File file1 = new File(drive + tempFileLocation +'/' + bufferSize + file);



            if (!file1.exists())
                file1.createNewFile();
            else file1.delete();

            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file1.getAbsolutePath()),bufferSize);


            //rng.nextBytes(bytes);
            long writtenBytes = 0;
            while (writtenBytes < fileSize && !getCancelled().get())
            {
                outputStream.write(bytes,0,bufferSize);
                writtenBytes+=bufferSize;
            }

            totalWritten+=writtenBytes;

            outputStream.close();

            file1.deleteOnExit();

            if (getCancelled().get())
                return 0;
        }


        long elapsed = timer.stop();

        return (totalWritten / 1024.0 / 1024) / (elapsed / Math.pow(10,9));
    }

    @Override
    public void run()
    {


        int iterations = runIterations;

        if (fileSize < idealFileSize)
            iterations = (int) (((idealFileSize)/fileSize) *runIterations);

        if (iterations > maxRunIterations)
            iterations = maxRunIterations;

        ArrayList<Double> writeSpeeds  = new ArrayList<>();

        try
        {
            for (int i=0;i<iterations&&!getCancelled().get();i++)
            {
                getProgressStatus().setValue("Running " + i + "/" + iterations);
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
        int iterations = maxWarmupIterations;

        if (fileSize < idealFileSize)
            iterations = (int) (((idealFileSize)/fileSize) *warmupIterations);

        if (iterations > maxWarmupIterations)
            iterations = maxWarmupIterations;


        int reductionFactor = 8;

        try
        {
            runningProgress.setValue(0);
            fileSize = fileSize/reductionFactor;
            for (int i=0;i<iterations&&!getCancelled().get();i++)
            {
                getProgressStatus().setValue("Warming up " + i + "/" + iterations);
                write("warmup"+i+".dat");
                runningProgress.setValue(runningProgress.get() + 0.5/iterations);
            }

            fileSize = fileSize*reductionFactor;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }
}
