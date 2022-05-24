package net.darktree.core.client.render;

public abstract class Screen {

	private boolean closed;

	public Screen() {
		closed = false;
	}

	public abstract void draw();

	public void onKey(int key, int action, int mods) {

	}

	public void onClick(int button, int action, int mods) {

	}

	public void onScroll(float value) {

	}

	public void onMove(float x, float y) {

	}

	public void onResize(int w, int h) {

	}

	public void close() {
		this.closed = true;
	}

	public boolean isClosed() {
		return this.closed;
	}

}
