package bench;

public interface IBenchmark {
	/**
	 * Runs the benchmark algorithm
	 */
	public void run();

	/**
	 *
	 * @param params
	 */
	public void run(Object ... params);

	/**
	 *
	 * @param params
	 */

	/**
	 * Initialize the benchmark
	 * @param params any type or number of arguments
	 */
	public void initialize(Object ... params);
	public void clean();
	
	/**
	 * Display the benchmark prior to the algorithm being run
	 */
	public void display();
	public void cancel();
}
