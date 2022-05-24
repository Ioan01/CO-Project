package upt.coproject.benchmark;

import upt.coproject.timing.Timer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class RandomWriteDriveBenchmark extends Benchmark {

    private static int bufferSize = 4 * 1024;
    private String drive;
    private int fileSize;

    private static int runIterations = 3;
    private static int maxRunIterations = 10;


    private static int warmupIterations = 10;
    private static int maxWarmupIterations = 40;
    private static double idealFileSize = 512.0 * 1024*1024;


    public RandomWriteDriveBenchmark() {
        super("Random Write");
    }

    public void initialize(String drive,int fileSize)
    {
        this.drive = drive;
        this.fileSize = fileSize;
    }

    private double write(String file) throws IOException
    {
        Random rng = new Random();
        String tempFileLocation = "benchmark/temp/seq" + rng.nextInt();
        File outputFolder = new File(drive + tempFileLocation);
        if (!outputFolder.isDirectory())
        {
            outputFolder.mkdirs();
        }

        Timer timer = new Timer();

        long totalWritten = 0;

        byte[] bytes = new byte[bufferSize];



        File file1 = new File(drive + tempFileLocation +'/' + bufferSize + file);
        RandomAccessFile randomAccessFile = new RandomAccessFile(file1,"rw");


        timer.start();

        while (totalWritten < fileSize && !getCancelled().get())
        {
            rng.nextBytes(bytes);

            randomAccessFile.write(bytes);

            randomAccessFile.seek((rng.nextInt() & Integer.MAX_VALUE) % fileSize);


            totalWritten += bufferSize;
        }

        randomAccessFile.close();

        file1.deleteOnExit();
        long elapsed = timer.stop();

        return (totalWritten / 1024.0 / 1024) / (elapsed / Math.pow(10,9));
    }


    @Override
    public void run() {




        int iterations = runIterations;

        if (fileSize < idealFileSize)
            iterations = (int) (((idealFileSize)/fileSize) *runIterations);

        if (iterations > maxRunIterations)
            iterations = maxRunIterations;




        int reductionFactor = 4;

        fileSize = fileSize/ reductionFactor;

        ArrayList<Double> writeSpeeds  = new ArrayList<>();

        try
        {
            for (int i=0;i<iterations&&!getCancelled().get();i++)
            {
                writeSpeeds.add(write("random-output"+i+".dat"));

                runningProgress.setValue(runningProgress.get() + 0.5/iterations);
            }

            double avg = writeSpeeds.stream().reduce(0.0, Double::sum) / writeSpeeds.size();
            results.put("RND_WRITE",avg);
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
    public void clean() {

    }

    @Override
    public void warmup() {

        int reducingFactor =32;

        int iterations = maxWarmupIterations;

        if (fileSize < idealFileSize)
            iterations = (int) (((idealFileSize)/fileSize) *warmupIterations);

        if (iterations > maxWarmupIterations)
            iterations = maxWarmupIterations;


        try
        {
            runningProgress.setValue(0);
            fileSize = fileSize/reducingFactor;
            for (int i=0;i<iterations&&!getCancelled().get();i++)
            {
                write("rnd-warmup"+i+".dat");
                runningProgress.setValue(runningProgress.get() + 0.5/iterations);
            }

            fileSize = fileSize *reducingFactor;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}
