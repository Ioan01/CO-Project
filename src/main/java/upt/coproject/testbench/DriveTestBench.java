package upt.coproject.testbench;

import upt.coproject.benchmark.DriveReadBenchmark;
import upt.coproject.benchmark.SequentialWriteDriveBenchmark;

public class DriveTestBench extends TestBench
{
    public void initialize(String drive,long bufferSizeStart,long bufferSizeEnd,int fileSize)
    {
        benchmarks.clear();

        SequentialWriteDriveBenchmark writeDriveBenchmark = new SequentialWriteDriveBenchmark();
        writeDriveBenchmark.initialize(drive,bufferSizeStart,bufferSizeEnd,fileSize);
        benchmarks.add(writeDriveBenchmark);

        DriveReadBenchmark driveReadBenchmark = new DriveReadBenchmark();
        driveReadBenchmark.initialize(drive, bufferSizeStart, fileSize);
        benchmarks.add(driveReadBenchmark);


        trackRunningProgress();

    }
}
