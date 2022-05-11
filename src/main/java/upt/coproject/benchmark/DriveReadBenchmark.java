package upt.coproject.benchmark;

import java.io.*;
import java.nio.Buffer;
import java.util.Arrays;
import java.util.Random;

public class DriveReadBenchmark extends Benchmark{
    String drive;
    int bufferSizeMax;
    int bufferSize;
    int fileSize;
    File readFile;
    byte[] buffer;

    public DriveReadBenchmark(String name) {
        super(name);
        this.setName(name);
    }

    public void initialize(String drive, int bufferSizeMax, int fileSize) {
        this.drive = drive;
        this.bufferSizeMax = bufferSizeMax;
        this.fileSize = fileSize;
        this.buffer = new byte[bufferSizeMax];
        this.readFile = new File(this.getName());

        Random random = new Random();
        random.nextBytes(buffer);

        try {
            if(readFile.exists())
                readFile.delete();
            readFile.createNewFile();
            RandomAccessFile writer = new RandomAccessFile(this.getName(), "rw");
            int N = fileSize / bufferSizeMax;
            for (int i = 0; i < N; i++) {
                writer.seek(i * bufferSizeMax);
                writer.write(buffer);
            }
            writer.close();//*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        //byte[] flushCache = new byte[(int) Runtime.getRuntime().];
    }

    public void setBufferSize(int bufferSize){
        this.bufferSize = bufferSize;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = new FileInputStream(this.getName());
            int N = fileSize / bufferSize;
            while (inputStream.read(buffer, 0, bufferSize) != -1){
                // keep going
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(Arrays.toString(this.buffer));
    }

    @Override
    public void clean() {

    }

    @Override
    public void warmup() {

    }


}
