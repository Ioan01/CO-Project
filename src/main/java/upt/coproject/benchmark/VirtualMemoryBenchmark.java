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


	public VirtualMemoryBenchmark()
	{
		super("VirtualMemoryBenchmark");
	}

	private String result = "";
	MemoryMapper core = null;

	//@Override
	public void initialize(Object... params) {
	}

	@Override
	public void warmup() {
	}

	@Override
	public void run() {
		throw new UnsupportedOperationException("Use run(Object[]) instead");
	}

	//@Override
	public void run(Object... options) {
		// expected example: {fileSize, bufferSize}
		Object[] params = (Object[]) options;
		long fileSize = Long.parseLong(params[0].toString()); // e.g. 2-16GB
		int bufferSize = Integer.parseInt(params[1].toString()); // e.g. 4+KB


		try {
			core = new MemoryMapper("C:\\_core", fileSize);
			byte[] buffer = new byte[bufferSize];
			Random rand = new Random();

			Timer timer = new Timer();

			// write to VM
			timer.start();
			for (long i = 0; i < fileSize; i += bufferSize) {
				// generate random content (see assignments 9,11)

				Random rng = new Random();

				double totalWritten = 0;

				byte[] bytes = new byte[(int) bufferSize[bufferSize.length-1]];
				rng.nextBytes(bytes);
				for (int bS:bufferSize) {





					//rng.nextBytes(bytes);
					long writtenBytes = 0;
					while (writtenBytes < fileSize && !getCancelled().get())
					{
						outputStream.write(bytes,0,bufferSize);
						writtenBytes+=bytes.length;
					}



					outputStream.close();
					totalWritten+=file1.length();
					file1.deleteOnExit();

					if (getCancelled().get())
						return 0;
				}


				long elapsed = timer.stop();

				return (totalWritten / 1024 / 1024) / (elapsed / Math.pow(10,9));
				// write to memory mapper
			}
			double speed = 0.0; /* fileSize/time MB/s */

			result = "\nWrote " + (fileSize / 1024 / 1024L)
					+ " MB to virtual memory at " + /*speed, with exactly 2 decimals*/ +" MB/s";

			// read from VM
			timer.start();
			for (long i = 0; i < fileSize; i += bufferSize) {
				// get from memory mapper
			}
			speed = 0.0; /* MB/s */

			// append to previous 'result' string
			result += "\nRead " + (fileSize / 1024 / 1024L)
					+ " MB from virtual memory at " + /*speed, with exactly 2 decimals*/ +" MB/s";

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (core != null)
				core.purge();
		}
	}

	@Override
	public void clean() {
		if (core != null)
			core.purge();
	}

	//@Override
	public String getResult() {
		return (1 == 1) ? "result" : result;
	}

}
