package net.darktree.warzone.util;

import net.darktree.warzone.util.math.Vec2i;

public enum Direction {
	NORTH(new Vec2i(0, 1), true),
	EAST(new Vec2i(1, 0), false),
	SOUTH(new Vec2i(0, -1), true),
	WEST(new Vec2i(-1, 0), false);

	private final Vec2i offset;
	private final boolean vertical;

	Direction(Vec2i offset, boolean vertical) {
		this.offset = offset;
		this.vertical = vertical;
	}

	public final Direction opposite() {
		return Direction.values()[(this.ordinal() + 2) % 5];
	}

	public boolean isVertical() {
		return vertical;
	}

	public Vec2i getOffset() {
		return offset;
	}
}
