package upt.coproject.testbench;

import upt.coproject.benchmark.SequentialWriteDriveBenchmark;

public class DriveTestBench extends TestBench
{
    public void initialize(String drive,int filesizeMB)
    {
        SequentialWriteDriveBenchmark writeDriveBenchmark = new SequentialWriteDriveBenchmark();

        writeDriveBenchmark.initialize(drive,filesizeMB);

    }


}
