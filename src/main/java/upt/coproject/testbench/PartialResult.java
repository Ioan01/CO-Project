package upt.coproject.testbench;

public class PartialResult {
    public int bufferSize; // KB
    public int memorySize; // KB
    public double time; // millisecond
    public double speed; // MB/s

    public PartialResult(long bufferSize, long memorySize, double time) {
        this.bufferSize = (int) bufferSize / 1024;
        this.memorySize = (int) memorySize / 1024;
        this.time = time;
        this.speed = (double) this.memorySize / time * 1000 / 1024;
    }
}
