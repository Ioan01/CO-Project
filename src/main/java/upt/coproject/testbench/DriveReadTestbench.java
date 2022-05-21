package upt.coproject.testbench;

import upt.coproject.benchmark.SequentialReadDriveBenchmark;

public class DriveReadTestbench extends TestBench{
    private static final int KB = 1024;
    private static final int MB = 1024 * 1024;
    private static final int GB = 1024 * 1024 * 1024;

    public static void main(String[] args) {
        SequentialReadDriveBenchmark bench = new SequentialReadDriveBenchmark();
        int fileSizeCnt = 8;
        int fileUnit = MB;

        bench.initialize("C://", fileSizeCnt * fileUnit);

        bench.run();
    }
}
