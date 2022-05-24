package upt.coproject;

import lombok.Getter;

public class PartialResult {
    @Getter
    private int buffer; // KB
    @Getter
    private int size; // KB
    @Getter
    private double time; // millisecond
    @Getter
    private double speed; // MB/s

    public PartialResult(long buffer, long size, double time) {
        this.buffer = (int) buffer / 1024;
        this.size = (int) size / 1024;
        this.time =  Math.round(time);
        this.speed = Math.round((double) this.size / time * 1000 / 1024);
    }
}
