package net.darktree.core.client.window;

public class FrameCounter {

	private long frames;
	private long time;
	private int rate, count;

	public void next() {
		long now = System.currentTimeMillis();

		if (now - time >= 1000) {
			time = now;
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

}
