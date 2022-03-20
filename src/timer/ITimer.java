package timing;

public interface ITimer {
    public void start();
    public long stop();
    public void resume();
    public long pause();
}
