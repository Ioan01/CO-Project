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
        DriveReadBenchmark bench = new DriveReadBenchmark("readbench.temp");
        bench.initialize("C://", 1024 * KB, 64 * MB);

        Timer timer = new Timer();

        bench.setBufferSize(1024 * KB);

        timer.start();
        bench.run();
        timer.stop();
        System.out.println(timer.getTime(TimeUnit.MILLI));
        timer.start();
        bench.run();
        timer.stop();
        System.out.println(timer.getTime(TimeUnit.MILLI));
        timer.start();
        bench.run();
        timer.stop();
        System.out.println(timer.getTime(TimeUnit.MILLI));
        timer.start();
        bench.run();
        timer.stop();
        System.out.println(timer.getTime(TimeUnit.MILLI));
        timer.start();
        bench.run();
        timer.stop();
        System.out.println(timer.getTime(TimeUnit.MILLI));
        timer.start();
        bench.run();
        timer.stop();
        System.out.println(timer.getTime(TimeUnit.MILLI));
        timer.start();
        bench.run();
        timer.stop();
        System.out.println(timer.getTime(TimeUnit.MILLI));
        timer.start();
        bench.run();
        timer.stop();
        System.out.println(timer.getTime(TimeUnit.MILLI));
        timer.start();
        bench.run();
        timer.stop();
        System.out.println(timer.getTime(TimeUnit.MILLI));
    }

    @Override
    public void clean() {

    }
}
