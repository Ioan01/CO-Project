package upt.coproject.testbench;

import upt.coproject.benchmark.DriveReadBenchmark;
import upt.coproject.benchmark.RandomWriteDriveBenchmark;
import upt.coproject.benchmark.SequentialWriteDriveBenchmark;

public class DriveTestBench extends TestBench
{
    public void initialize(String drive,long fileSize)
    {
        benchmarks.clear();

        SequentialWriteDriveBenchmark writeDriveBenchmark = new SequentialWriteDriveBenchmark();
        RandomWriteDriveBenchmark randomWriteDriveBenchmark = new RandomWriteDriveBenchmark();


        randomWriteDriveBenchmark.initialize(drive, (int) fileSize);
        writeDriveBenchmark.initialize(drive,fileSize);
        benchmarks.add(randomWriteDriveBenchmark);
        benchmarks.add(writeDriveBenchmark);

        //DriveReadBenchmark driveReadBenchmark = new DriveReadBenchmark();
       // driveReadBenchmark.initialize(drive, bufferSizeStart, fileSize);
        //benchmarks.add(driveReadBenchmark);

        trackRunningProgress();

    }
}
