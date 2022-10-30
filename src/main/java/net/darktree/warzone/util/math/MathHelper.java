package net.darktree.warzone.util.math;

import net.darktree.warzone.util.Direction;
import net.darktree.warzone.world.tile.TilePos;

import java.util.Random;

public class MathHelper {

	public static Random RANDOM = new Random();

	public static int nextRandomDice() {
		return RANDOM.nextInt(1, 7);
	}

	public static <T extends Enum<T>> T getRandomEnum(Class<T> enumerable) {
		T[] values = enumerable.getEnumConstants();
		return values[RANDOM.nextInt(values.length)];
	}

	/**
	 * Returns a manhattan distance between two points
	 */
	public static int getManhattanDistance(int x1, int y1, int x2, int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}

	/**
	 * Returns a chebyshev distance between two points
	 */
	public static int getChebyshevDistance(int x1, int y1, int x2, int y2) {
		return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
	}

	/**
	 * Returns a standard distance between two points
	 */
	public static float getStandardDistance(int x1, int y1, int x2, int y2) {
		int vx = x1 - x2;
		int vy = y1 - y2;

		return (float) Math.sqrt(vx * vx + vy * vy);
	}

	/**
	 * Returns an approximated middle grid point between two points
	 */
	public static TilePos getMiddlePoint(int x1, int y1, int x2, int y2) {
		return new TilePos((x1 + x2) / 2, (y1 + y2) / 2);
	}

	/**
	 * Get a direction pointing from (x1, y1) towards (x2, y2)
	 * Use only for points on an axis-aligned line
	 */
	public static Direction getDirection(int x1, int y1, int x2, int y2) {
		if (x2 > x1) return Direction.EAST;
		if (x2 < x1) return Direction.WEST;
		if (y2 > y1) return Direction.NORTH;
		if (y2 < y1) return Direction.SOUTH;

		throw new RuntimeException("The given points are equal!");
	}

}
