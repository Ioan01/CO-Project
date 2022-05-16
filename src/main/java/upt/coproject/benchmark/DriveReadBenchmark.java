package upt.coproject.benchmark;

import upt.coproject.timing.TimeUnit;
import upt.coproject.timing.Timer;

import java.io.*;
import java.util.Random;

public class DriveReadBenchmark extends Benchmark{
    private static final int KB = 1024;
    private static final int MB = 1024 * 1024;
    private static final int GB = 1024 * 1024 * 1024;
    String drive;
    long bufferSizeMax;
    long bufferSize;
    long fileSize;
    String folderName = "temp";


    public DriveReadBenchmark() {
        super("Sequential drive read benchmark");
        this.setName("Sequential drive read benchmark");
    }

    public void initialize(String drive, long bufferSizeMax, long fileSize) {
        File folder = new File(drive + folderName);
        if (!folder.exists()) {
            folder.mkdir();
        }
        folder.deleteOnExit();

        this.drive = drive + folderName + "/";
        this.bufferSizeMax = bufferSizeMax; // legacy
        this.bufferSize = bufferSizeMax; // legacy
        this.fileSize = fileSize;
    }

    @Override
    public void run() {
        int N = 20;

        long sizeTotal = 0;
        double timeTotal = 0;

        double speedTotalRead = 0;
        double speedTotalWrite = 0;

        for (int i = 0; i < N; i++) {
            double speed = this.write("temp_file_no_"+i);
            speedTotalWrite += speed;
            System.out.println("write speed: " + speed);

            runningProgress.setValue(runningProgress.get() + 0.25 / N);
        }

        for (int i = 0; i < N; i++) {
            double speed = this.read("temp_file_no_"+i);
            speedTotalRead += speed;
            System.out.println("read speed: " + speed);

            runningProgress.setValue(runningProgress.get() + 0.25 / N);
        }
        System.out.println("    AVG WRITE SPEED: |" + speedTotalWrite / N + "|");
        System.out.println("    AVG READ SPEED: |" + speedTotalRead / N + "|");
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
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(drive + filename), (int) bufferSizeMax);
            long N = fileSize / bufferSizeMax;

            timer.start();
            for (int i = 0; i < N; i++) {
                timer.pause();
                byte[] buffer = new byte[(int)bufferSizeMax];
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
        //long flushSize = 8 * GB;
        int flushBufferSize = MB;
        String flushFileName = "flush_file";

        File file = new File(drive + flushFileName);
        //file.deleteOnExit();

        try{
            if(file.exists())
                file.delete();
            file.createNewFile();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(drive + flushFileName), flushBufferSize);

            Timer timer = new Timer();
            Random random = new Random(System.nanoTime());
            int N = 6 * KB;
            timer.start();
            for (int i = 0; i < N; i++) {
                timer.pause();
                byte[] buffer = new byte[flushBufferSize];
                random.nextBytes(buffer);
                timer.resume();
                bufferedOutputStream.write(buffer);
                //System.out.println(i + " " + buffer.length);
            }
            timer.stop();
            System.out.println("        FLUSH WRITE SPEED " + N / timer.getTime(TimeUnit.SEC));
            bufferedOutputStream.flush();
            bufferedOutputStream.close();



            InputStream inputStream = new FileInputStream(drive + flushFileName);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, flushBufferSize);
            byte[] buffer = new byte[flushBufferSize];
            timer.start();
            while (bufferedInputStream.read(buffer, 0, flushBufferSize) != -1){
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
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(drive + filename), (int)bufferSize);
            byte[] buffer = new byte[(int)bufferSize];

            timer.start();
            while (bufferedInputStream.read(buffer, 0, (int) bufferSize) != -1){
                // keep going
            }
            timer.stop();

            bufferedInputStream.close();
            return (double) fileSize / MB / timer.getTime(TimeUnit.SEC);
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
