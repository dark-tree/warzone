package net.darktree.core.client.window;

public class FrameCounter {

	private long frames, time;
	private long timer, nanos;
	private int rate, count;

	public void next() {
		long now = System.currentTimeMillis();

		long stamp = System.nanoTime();
		time = (int) (stamp - nanos);
		nanos = stamp;

		if (now - timer >= 1000) {
			timer = now;
			rate = count;
			count = 0;
		}

		count ++;
		frames ++;
	}

	public int getFrameRate() {
		return rate;
	}

	public long getFrameCount() {
		return frames;
	}

	public long getFrameTime() {
		return time;
	}

}
