package upt.coproject.testbench;

import upt.coproject.benchmark.SequentialWriteDriveBenchmark;

public class DriveTestBench extends TestBench
{
    public void initialize(String drive,long bufferSizeStart,long bufferSizeEnd,int fileSize)
    {
        SequentialWriteDriveBenchmark writeDriveBenchmark = new SequentialWriteDriveBenchmark();

        writeDriveBenchmark.initialize(drive,bufferSizeStart,bufferSizeEnd,fileSize);
        benchmarks.clear();
        benchmarks.add(writeDriveBenchmark);


        trackRunningProgress();

    }


}
