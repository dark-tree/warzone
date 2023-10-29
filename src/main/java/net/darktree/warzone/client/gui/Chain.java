package net.darktree.warzone.client.gui;

public enum Chain {
	AFTER(1, 0, 0, 0),
	BEFORE(0, 0, -1, 0),
	ABOVE(0, 1, 0, 0),
	BELOW(0, 0, 0, -1),
	OVER(0, 0, 0, 0);

	private final int px, py;
	private final int nx, ny;

	Chain(int px, int py, int nx, int ny) {
		this.px = px;
		this.py = py;
		this.nx = nx;
		this.ny = ny;
	}

	public int nextX(int x, int pw, int nw) {
		return pw * this.px + nw * this.nx + x;
	}

	public int nextY(int y, int ph, int nh) {
		return ph * this.py + nh * this.ny + y;
	}
}
