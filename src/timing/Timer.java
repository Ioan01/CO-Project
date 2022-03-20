package timing;

public class Timer implements ITimer{
	private long total_t;
	private long start_t;

    public Timer(){
        
    }

    public void start(){
        this.total_t = 0;
        this.start_t = System.nanoTime();
    }

    public long stop(){
    	long stop_t = System.nanoTime();
    	this.total_t += stop_t - this.start_t;
    	
        return total_t;
    }

    public void resume(){
    	this.start_t = System.nanoTime();
    }

    public long pause(){
    	long pause_t = System.nanoTime();
    	long elapsed_t = pause_t - this.start_t;
    	this.total_t += elapsed_t;
    	
        return elapsed_t;
    }
}
