package upt.coproject.testbench;

import upt.coproject.benchmark.DriveReadBenchmark;
import upt.coproject.benchmark.MockBench;
import upt.coproject.timing.TimeUnit;
import upt.coproject.timing.Timer;

public class DriveReadTestbench extends TestBench{
    private static final int KB = 1024;
    private static final int MB = 1024 * 1024;
    private static final int GB = 1024 * 1024 * 1024;

    public static void main(String[] args) {
        System.out.println("Free RAM intial: " + Runtime.getRuntime().freeMemory() / MB);
        System.out.println("Free HEAP intial: " + Runtime.getRuntime().totalMemory() / MB);

        DriveReadBenchmark bench = new DriveReadBenchmark();
        int bufferSizeMaxCnt = 16;
        int bufferUnit = KB;
        int fileSizeCnt = 8;
        int fileUnit = MB;
        int N = 5;
        int bufferSizeMax = bufferSizeMaxCnt * bufferUnit;
        int fileSize = fileSizeCnt * fileUnit;

        bench.initialize("C://", bufferSizeMax, fileSize);

        bench.run();
    }
}
