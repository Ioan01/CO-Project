package upt.coproject.timing;

public interface ITimer {
    void start();
    /**
     * @return total running time in nanoseconds
     */
    long stop();
    void resume();
    /**
     * @return run time since last call of start() or resume() in nanoseconds
     */
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
