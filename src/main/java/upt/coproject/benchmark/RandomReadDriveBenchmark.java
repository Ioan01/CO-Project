package upt.coproject.benchmark;

import lombok.Getter;
import upt.coproject.PartialResult;
import upt.coproject.timing.TimeUnit;
import upt.coproject.timing.Timer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomReadDriveBenchmark extends Benchmark{
    private static final int KB = 1024;
    private static final int MB = 1024 * 1024;
    private static final int GB = 1024 * 1024 * 1024;
    String drive;
    long fileSize;
    long fileCount;
    String folderName = "temp_rnd_read";
    String miniFolderNameTemplate = "temp_file_no_";
    String fileNameTemplate = "temp_file_no_";
    long totalFilesSize = 128 * MB;
    List<String> filenames;
    long[] bufferSizes = {4 * KB};
    long writeBufferSize = MB;
    int sparsityFactor = 8;
    @Getter
    private List<PartialResult> partialResults = new ArrayList<>();

    public RandomReadDriveBenchmark() {
        super("Random drive read benchmark");
        this.setName("Random drive read benchmark");
        this.filenames = new ArrayList<>();
    }

    public void initialize(String drive, long fileSize) {
        File folder = new File(drive + folderName);
        if (!folder.exists()) {
            folder.mkdir();
        }
        folder.deleteOnExit();

        this.drive = drive + folderName + "/";
        this.fileSize = fileSize;

        if(fileSize > totalFilesSize){
            this.fileCount = 1;
        }
        else{
            this.fileCount = this.totalFilesSize / this.fileSize * sparsityFactor;
            System.out.println();
        }
    }

    @Override
    public void run() {
        System.out.println("fielSize: " + fileSize / MB);
        System.out.println("fielCount: " + fileCount);
        System.out.print("bufferSizes: ");
        for(long bufferSize: bufferSizes){
            System.out.print(bufferSize / KB + " ");
        }
        System.out.println();

        double speedTotalRead = 0;
        double speedTotalWrite = 0;


        for (int i = 0; i < fileCount; i++) {
            for(int j = 0; j < bufferSizes.length; j++){
                double speed = this.write(fileNameTemplate + i + "_" + bufferSizes[j]);
                speedTotalWrite += speed;

                getProgressStatus().setValue("Prepping " + (i*bufferSizes.length + j) + "/" + fileCount * bufferSizes.length);
                runningProgress.setValue(runningProgress.get() + 0.5 / (fileCount * bufferSizes.length));
            }
        }

        System.out.println("    AVG WRITE SPEED RND: |" + speedTotalWrite / (fileCount * bufferSizes.length) + "|");

        //flush_cache();

        ArrayList<Double> readSpeeds  = new ArrayList<>();

        for (int i = 0; i < fileCount; i++) {
            for(int j = 0; j < bufferSizes.length; j++) {
                double speed = this.read(fileNameTemplate + i + "_" + bufferSizes[j], bufferSizes[j]);
                speedTotalRead += speed;
                readSpeeds.add(speed);
                //System.out.println("read speed: " + speed);

                getProgressStatus().setValue("Running " + (i*bufferSizes.length + j) + "/" + fileCount * bufferSizes.length);
                runningProgress.setValue(runningProgress.get() + 0.5 / (fileCount * bufferSizes.length));
            }
        }
        System.out.println("    AVG READ SPEED RND: |" + speedTotalRead / (fileCount * bufferSizes.length) + "|");
        results.put("RND_READ", speedTotalRead / (fileCount * bufferSizes.length));
    }

    public double write(String filename){
        File file = new File(drive + filename);
        file.deleteOnExit();

        Random random = new Random(System.nanoTime());
        Timer timer = new Timer();

        try {
            if(file.exists())
                file.delete();
            file.createNewFile();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(drive + filename), (int)writeBufferSize);

            timer.start();
            long iterations = Math.max(fileSize / writeBufferSize, 1);
            for (int i = 0; i < iterations; i++) {
                timer.pause();
                byte[] buffer = new byte[(int)writeBufferSize];
                random.nextBytes(buffer);
                timer.resume();
                bufferedOutputStream.write(buffer);
            }
            timer.stop();

            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (double) fileSize / MB / timer.getTime(TimeUnit.SEC);
    }

    public void flush_cache(){
        long flushSizeMB = 8 * 1024 * 1024;
        int flushBufferSizeKB = 1024;
        String flushFileName = "flush_file";

        File file = new File(drive + flushFileName);
        file.deleteOnExit();

        try{
            if(file.exists())
                file.delete();
            file.createNewFile();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(drive + flushFileName), flushBufferSizeKB);

            Timer timer = new Timer();
            Random random = new Random(System.nanoTime());
            long iterations = (flushSizeMB / flushBufferSizeKB) * 1024;
            timer.start();
            for (int i = 0; i < iterations; i++) {
                timer.pause();
                byte[] buffer = new byte[KB];
                random.nextBytes(buffer);
                timer.resume();
                bufferedOutputStream.write(buffer);
                //System.out.println(i + " " + buffer.length);
            }
            timer.stop();
            System.out.println("        FLUSH WRITE SPEED " + flushSizeMB/ timer.getTime(TimeUnit.SEC));
            bufferedOutputStream.flush();
            bufferedOutputStream.close();



            InputStream inputStream = new FileInputStream(drive + flushFileName);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, flushBufferSizeKB);
            byte[] buffer = new byte[flushBufferSizeKB];
            timer.start();
            while (bufferedInputStream.read(buffer, 0, flushBufferSizeKB) != -1){
                //keep going
            }
            timer.stop();
            bufferedInputStream.close();
            inputStream.close();
            System.out.println("        FLUSH READ SPEED " + 8 * KB / timer.getTime(TimeUnit.SEC));
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    public double read(String filename, long bufferSize) {
        Timer timer = new Timer();
        Random random = new Random(System.nanoTime());

        try {
            long totalReadSize = 0;

            File file = new File(drive + filename);
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            byte[] buffer = new byte[(int) bufferSize];
            long iterations = Math.max(this.fileSize / sparsityFactor / bufferSize, 1);

            long readSize = 0;

            timer.start();
            for(int i = 0; i < iterations; i++){
                int position = random.nextInt((int) this.fileSize - 1);
                randomAccessFile.seek(position);
                readSize += randomAccessFile.read(buffer, 0, (int) bufferSize);
            }
            timer.stop();

            totalReadSize += readSize;

            partialResults.add(new PartialResult(bufferSize, readSize, timer.getElapsedTime(TimeUnit.MILLI)));

            randomAccessFile.close();
            file.delete();

            return totalReadSize / MB / timer.getTime(TimeUnit.SEC);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public void clean() {

    }

    @Override
    public void warmup() {

    }
}
