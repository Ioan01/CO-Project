package upt.coproject;

import lombok.Getter;

public class Result {
    @Getter
    String model;
    @Getter
    int fileSize; // KB
    @Getter
    int score;

    public Result(String model, int fileSize, int score) {
        this.model = model;
        this.fileSize = fileSize / 1024;
        this.score = score;
    }
}
