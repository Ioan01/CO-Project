package upt.coproject.testbench;

import upt.coproject.benchmark.RandomReadDriveBenchmark;
import upt.coproject.benchmark.RandomWriteDriveBenchmark;
import upt.coproject.benchmark.SequentialReadDriveBenchmark;
import upt.coproject.benchmark.SequentialWriteDriveBenchmark;

import java.util.Map;

public class DriveTestBench extends TestBench
{
    public void initialize(String drive,long fileSize)
    {
        benchmarks.clear();

        SequentialReadDriveBenchmark sequentialReadDriveBenchmark = new SequentialReadDriveBenchmark();
        sequentialReadDriveBenchmark.initialize(drive, fileSize);
        benchmarks.add(sequentialReadDriveBenchmark);

        RandomReadDriveBenchmark randomReadDriveBenchmark = new RandomReadDriveBenchmark();
        randomReadDriveBenchmark.initialize(drive, fileSize);
        benchmarks.add(randomReadDriveBenchmark);

        SequentialWriteDriveBenchmark sequentialWriteDriveBenchmark = new SequentialWriteDriveBenchmark();
        sequentialWriteDriveBenchmark.initialize(drive,fileSize);
        benchmarks.add(sequentialWriteDriveBenchmark);

        RandomWriteDriveBenchmark randomWriteDriveBenchmark = new RandomWriteDriveBenchmark();
        randomWriteDriveBenchmark.initialize(drive, (int) fileSize);
        benchmarks.add(randomWriteDriveBenchmark);

        partialResults.put("SEQ_READ", sequentialReadDriveBenchmark.getPartialResults());
        partialResults.put("RND_READ", randomReadDriveBenchmark.getPartialResults());
        partialResults.put("SEQ_WRITE", sequentialWriteDriveBenchmark.getPartialResults());
        partialResults.put("RND_WRITE", randomWriteDriveBenchmark.getPartialResults());
        trackRunningProgress();
    }
}
