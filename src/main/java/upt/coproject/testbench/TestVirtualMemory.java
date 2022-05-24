package upt.coproject.testbench;

import upt.coproject.benchmark.VirtualMemoryBenchmark;

public class TestVirtualMemory extends TestBench{

    public static void main(String[] args) {
        VirtualMemoryBenchmark bench = new VirtualMemoryBenchmark();

        long fileSize = 2L * 1024 * 1024 * 1024;
        int bufferSize = 512*1024*1024;

        bench.initialize("C://", bufferSize, fileSize);
        bench.run();
        System.out.println(bench.getResult());

    }
}
