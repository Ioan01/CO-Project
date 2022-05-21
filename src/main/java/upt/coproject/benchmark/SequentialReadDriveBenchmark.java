package upt.coproject.benchmark;

import lombok.Getter;
import upt.coproject.PartialResult;
import upt.coproject.timing.TimeUnit;
import upt.coproject.timing.Timer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SequentialReadDriveBenchmark extends Benchmark{
    private static final int KB = 1024;
    private static final int MB = 1024 * 1024;
    private static final int GB = 1024 * 1024 * 1024;
    String drive;
    long fileSize;
    long fileCount;
    String folderName = "temp";
    String miniFolderNameTemplate = "temp_file_no_";
    String fileNameTemplate = "temp_file_no_";
    long totalFilesSize = 256 * MB;
    List<String> filenames;
    long[] bufferSizes = {16 * KB, 64 * KB, 256 * KB, MB};
    long writeBufferSize = MB;
    @Getter
    private List<PartialResult> partialResults = new ArrayList<>();

    public SequentialReadDriveBenchmark() {
        super("Sequential drive read benchmark");
        this.setName("Sequential drive read benchmark");
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
            this.fileCount = this.totalFilesSize / this.fileSize;
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
            double speed = this.write(fileNameTemplate + i);
            speedTotalWrite += speed;
            //System.out.println("write speed: " + speed);

            runningProgress.setValue(runningProgress.get() + 0.5 / fileCount);
        }
        System.out.println("    AVG WRITE SPEED: |" + speedTotalWrite / fileCount + "|");

        //flush_cache();

        ArrayList<Double> readSpeeds  = new ArrayList<>();

        for (int i = 0; i < fileCount; i++) {
            double speed = this.read(fileNameTemplate + i);
            speedTotalRead += speed;
            readSpeeds.add(speed);
            //System.out.println("read speed: " + speed);

            runningProgress.setValue(runningProgress.get() + 0.5 / fileCount);
        }
        System.out.println("    AVG READ SPEED: |" + speedTotalRead / fileCount + "|");
        results.put("SEQ_READ", speedTotalRead / fileCount);
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
            long iterations = fileSize / writeBufferSize;
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


    public double read(String filename) {
        Timer timer = new Timer();

        try {
            timer.start();
            timer.pause();

            for(long bufferSize: bufferSizes) {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(drive + filename), (int) bufferSize);
                byte[] buffer = new byte[(int) bufferSize];

                timer.resume();
                while (bufferedInputStream.read(buffer, 0, (int) bufferSize) != -1) {
                    // keep going
                }
                timer.pause();
                partialResults.add(new PartialResult(bufferSize, fileSize, timer.getElapsedTime(TimeUnit.MILLI)));

                bufferedInputStream.close();
            }
            timer.stop();

            File file = new File(drive + filename);
            file.delete();

            return (double) (fileSize * bufferSizes.length) / MB / timer.getTime(TimeUnit.SEC);
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
