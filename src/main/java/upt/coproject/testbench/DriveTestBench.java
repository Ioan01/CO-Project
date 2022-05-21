package upt.coproject.testbench;

import upt.coproject.benchmark.SequentialReadDriveBenchmark;
import upt.coproject.benchmark.SequentialWriteDriveBenchmark;

import java.util.Map;

public class DriveTestBench extends TestBench
{
    public void initialize(String drive,long fileSize)
    {
        benchmarks.clear();

        SequentialWriteDriveBenchmark writeDriveBenchmark = new SequentialWriteDriveBenchmark();
        writeDriveBenchmark.initialize(drive,fileSize);
        benchmarks.add(writeDriveBenchmark);

        SequentialReadDriveBenchmark sequentialReadDriveBenchmark = new SequentialReadDriveBenchmark();
        sequentialReadDriveBenchmark.initialize(drive, fileSize);
        benchmarks.add(sequentialReadDriveBenchmark);

        partialResults.put("SEQ_READ", sequentialReadDriveBenchmark.getPartialResults());

        trackRunningProgress();
    }
}
