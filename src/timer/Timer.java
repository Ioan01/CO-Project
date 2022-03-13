public class Timer implements ITimer{
    long total_t;
    long start_t;

    public Timer(){
        
    }

    public void start(){
        this.total_t = 0;
        this.start_t = System.nanoTime();
        System.out.println(start_t);
    }

    public long stop(){
        return -1;
    }

    public void resume(){

    }

    public long pause(){
        return -1;
    }
}
