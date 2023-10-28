package net.darktree.warzone.client.gui;

public enum Chain {
	AFTER(1, 0),
	BEFORE(-1, 0),
	ABOVE(0, 1),
	BELOW(0, -1),
	OVER(0, 0);

	private final int x, y;

	Chain(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int nextX(int x, int w) {
		return w * this.x + x;
	}

	public int nextY(int y, int h) {
		return h * this.y + y;
	}
}
