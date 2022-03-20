package timing;

public interface ITimer {
    void start();
    long stop();
    void resume();
    long pause();
    /**
     * @param unit time unit used for measurement
     * @return total running time between start() and the last call of pause() or stop()
     */
    double getTime(TimeUnit unit);
    /**
     * @param unit time unit used for measurement
     * @return running time between last call of start() or resume() and the last call of pause() or stop()
     */
    double getElapsedTime(TimeUnit unit);
}
