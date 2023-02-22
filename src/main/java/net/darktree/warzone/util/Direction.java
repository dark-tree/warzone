package net.darktree.warzone.util;

import net.darktree.warzone.util.math.Vec2i;

public enum Direction {
	NORTH(new Vec2i(0, 1), Axis.Y),
	EAST(new Vec2i(1, 0), Axis.X),
	SOUTH(new Vec2i(0, -1), Axis.Y),
	WEST(new Vec2i(-1, 0), Axis.X);

	private final Vec2i offset;
	private final Axis axis;

	Direction(Vec2i offset, Axis axis) {
		this.offset = offset;
		this.axis = axis;
	}

	public final Direction opposite() {
		return Direction.values()[(this.ordinal() + 2) % 4];
	}

	public Direction next() {
		return Direction.values()[(this.ordinal() + 1) % 4];
	}

	public Vec2i getOffset() {
		return offset;
	}

	public Axis getAxis() {
		return axis;
	}

	public enum Axis {
		X, Y;

		public boolean isVertical() {
			return this == Y;
		}

		public boolean isHorizontal() {
			return this == X;
		}

		public Direction asDirection() {
			return this == X ? EAST : NORTH;
		}
	}
}
