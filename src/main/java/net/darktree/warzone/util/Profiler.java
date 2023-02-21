package net.darktree.warzone.util;

public class Profiler {

	private final long time;

	private Profiler() {
		this.time = System.currentTimeMillis();
	}

	/**
	 * Create a new profiler, call {@link Profiler#getTime()} on the returned object to
	 * get the time in milliseconds since the object was created, alternatively call print
	 * {@link Profiler#print(String)} to log the elapsed time.
	 */
	public static Profiler start() {
		return new Profiler();
	}

	/**
	 * Returns the time in milliseconds since the object was instantiated
	 */
	public long getTime() {
		return System.currentTimeMillis() - this.time;
	}

	/**
	 * Replaces '%s' in the given string with the elapsed time in milliseconds and prints the message to the log
	 */
	public void print(String pattern) {
		Logger.info(pattern.formatted(getTime()));
	}

}
