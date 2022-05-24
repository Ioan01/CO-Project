package upt.coproject.benchmark;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import upt.coproject.benchmark.Benchmark;
import upt.coproject.timing.Timer;

/**
 * Maps a large file into RAM triggering the virtual memory mechanism. Perform
 * reads and writes to the respective file.<br>
 * The access speeds depend on the file size: if the file can fit the available
 * RAM, then we are measuring RAM speeds.<br>
 * Conversely, we are measuring the access speed of virtual memory, implying a
 * mixture of RAM and HDD access speeds (i.e., lower).
 */
public class VirtualMemoryBenchmark extends Benchmark {

	private String folderName = "tempRAM";
	private String drive;
	private int bufferSize;
	private long fileSize;

	public VirtualMemoryBenchmark() {
		super("VirtualMemoryBenchmark");
	}

	private String result = "";
	MemoryMapper core = null;

	public void initialize(String drive, int bufferSize, long fileSize) {
		File folder = new File(drive + folderName);
		if (!folder.exists()) {
			folder.mkdir();
		}
		folder.deleteOnExit();

		this.drive = drive + folderName + "/";
		this.bufferSize = bufferSize; // legacy
		this.fileSize = fileSize;
	}
	@Override
	public void warmup() {
	}

	@Override
	public void run() {

		try {
			core = new MemoryMapper(drive, fileSize);
			byte[] buffer = new byte[bufferSize];
			Random rand = new Random();

			Timer timer = new Timer();

			// write to VM
			timer.start();
			for (long i = 0; i < fileSize; i += bufferSize) {
				rand.nextBytes(buffer);
				core.put(i, buffer);    // write to memory mapper

				if (getCancelled().get()) {
					timer.stop();
					clean();
				}
			}

			double timeInSeconds = timer.stop() / 1000000000.0;
			double speed = (double) (fileSize / 1024 / 1024L) / timeInSeconds;	//	MB/s

			result = "\nWrote " + (fileSize / 1024 / 1024L)
					+ " MB to virtual memory at " + speed + " MB/s";

			// read from VM
			timer.start();
			for (long i = 0; i < fileSize; i += bufferSize) {
				buffer = core.get(i, bufferSize);

				if (getCancelled().get()) {
					timer.stop();
					clean();
				}
			}
			timeInSeconds = timer.stop() / 1000000000.0;
			speed = (double) (fileSize 	/ 1024 / 1024L) / timeInSeconds; /* fileSize/time MB/s */

			// append to previous 'result' string
			result += "\nRead " + (fileSize / 1024 / 1024L)
					+ " MB from virtual memory at " + speed + " MB/s";

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			clean();
		}

		clean();
	}

	@Override
	public void clean() {
		if (core != null)
			core.purge();

	}

	public String getResult() {
		return result;
	}

}
