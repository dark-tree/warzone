package net.darktree.core.util;

public enum Direction {
	NORTH(0, 1),
	EAST(1, 0),
	SOUTH(0, -1),
	WEST(-1, 0);

	public final int x, y;

	Direction(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public final Direction opposite() {
		return Direction.values()[(this.ordinal() + 2) % 5];
	}
}
