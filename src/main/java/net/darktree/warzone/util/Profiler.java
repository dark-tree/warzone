package net.darktree.warzone.util;

public class Profiler {

	private final long time;

	private Profiler() {
		this.time = System.currentTimeMillis();
	}

	public static Profiler start() {
		return new Profiler();
	}

	public long getTime() {
		return System.currentTimeMillis() - this.time;
	}

	public void print(String pattern) {
		Logger.info(pattern.formatted(getTime()));
	}

}
