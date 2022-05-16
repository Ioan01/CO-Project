package upt.coproject.testbench;

import upt.coproject.benchmark.MockBench;

public class MockTestBench extends TestBench
{



    public void initialize(double secondsToSleep)
    {
        MockBench mockBench = new MockBench("mockBench");
        mockBench.initialize(secondsToSleep);
        benchmarks.add(mockBench);

        MockBench mockBench2 = new MockBench("mockBench");
        mockBench2.initialize(secondsToSleep);
        benchmarks.add(mockBench2);

        MockBench mockBench3 = new MockBench("mockBench");
        mockBench3.initialize(secondsToSleep);
        benchmarks.add(mockBench3);

        trackRunningProgress();
    }
}
