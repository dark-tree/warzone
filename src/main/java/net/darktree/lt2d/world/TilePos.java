package net.darktree.lt2d.world;

public class TilePos {
	public final int x, y;

	public TilePos(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return x + " " + y;
	}
}
