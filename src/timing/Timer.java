package timing;

public class Timer implements ITimer{
	private long total_t;
	private long start_t;
    private long elapsed_t;

    public Timer(){

    }

    public void start(){
        this.total_t = 0;
        this.elapsed_t = 0;
        this.start_t = System.nanoTime();
    }

    public long stop(){
        this.elapsed_t = System.nanoTime() - this.start_t;
    	this.total_t += this.elapsed_t;

        return total_t;
    }

    public void resume(){
    	this.start_t = System.nanoTime();
    }

    public long pause(){
    	this.elapsed_t = System.nanoTime() - this.start_t;
    	this.total_t += this.elapsed_t;
    	
        return elapsed_t;
    }

    public double getTime(TimeUnit unit){
        return (double)this.total_t / unit.getValue();
    }

    public double getElapsedTime(TimeUnit unit){
        return (double)this.elapsed_t / unit.getValue();
    }
}
