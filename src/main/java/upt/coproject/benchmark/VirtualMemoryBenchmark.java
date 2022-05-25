package upt.coproject.benchmark;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import upt.coproject.RAM_Controller;
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

	@Getter
	@Setter
	protected DoubleProperty runningProgress = new SimpleDoubleProperty(0);

	public VirtualMemoryBenchmark() {
		super("VirtualMemoryBenchmark");
	}

	private String result = "";
	MemoryMapper core = null;

	public static double speedRead = 0, speedWrite = 0;

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
				runningProgress.setValue((float)i/fileSize);
				if (getCancelled().get()) {
					timer.stop();
					clean();
				}
			}
			runningProgress.setValue(1);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			runningProgress.setValue(0);

			double timeInSeconds = timer.stop() / 1000000000.0;
			speedWrite = (double) (fileSize / 1024 / 1024L) / timeInSeconds;	//	MB/s

			result = "\nWrote " + (fileSize / 1024 / 1024L)
					+ " MB to virtual memory at " + speedWrite + " MB/s";

			// read from VM
			timer.start();
			for (long i = 0; i < fileSize; i += bufferSize) {
				buffer = core.get(i, bufferSize);
				runningProgress.setValue((float)i/fileSize);
				if (getCancelled().get()) {
					timer.stop();
					clean();
				}
			}
			runningProgress.setValue(1);
			timeInSeconds = timer.stop() / 1000000000.0;
			speedRead = (double) (fileSize 	/ 1024 / 1024L) / timeInSeconds; /* fileSize/time MB/s */

			// append to previous 'result' string
			result += "\nRead " + (fileSize / 1024 / 1024L)
					+ " MB from virtual memory at " + speedRead + " MB/s";

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
		runningProgress.setValue(1);

	}

	@Override
	public void cancel(){
		getCancelled().setValue(true);
	}

	public String getResult() {
		return result;
	}

}
