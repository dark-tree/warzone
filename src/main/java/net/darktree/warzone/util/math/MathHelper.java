package net.darktree.warzone.util.math;

import net.darktree.warzone.util.Direction;
import net.darktree.warzone.world.tile.TilePos;

import java.util.List;
import java.util.Random;

public class MathHelper {

	public static final Random RANDOM = new Random();

	/**
	 * Roll a standard K6 dice
	 */
	public static int nextRandomDice(boolean weigh) {
		int dice = RANDOM.nextInt(1, 7);

		if (dice == 1 && weigh) {
			return nextRandomDice(true);
		}

		return dice;
	}

	/**
	 * Pick a random value from an enum
	 */
	public static <T extends Enum<T>> T randomEnumPick(Class<T> enumerable) {
		T[] values = enumerable.getEnumConstants();
		return values[RANDOM.nextInt(values.length)];
	}

	/**
	 * Pick a random entry from a list
	 */
	public static <T> T randomListPick(List<T> list) {
		return list.get(RANDOM.nextInt(list.size()));
	}

	/**
	 * clamp value to range (inclusive)
	 */
	public static int clamp(int value, int min, int max) {
		if (value < min) return min;
		return Math.min(value, max);
	}

	/**
	 * clamp value to range (inclusive)
	 */
	public static float clamp(float value, float min, float max) {
		if (value < min) return min;
		return Math.min(value, max);
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

	/**
	 * Return the value of a sigmoid for the given x,
	 * this functions returns values from 0 to 1, with 0.5 at x=0
	 */
	public static float sigmoid(float x) {
		return (float) (1 / (1 + Math.pow(Math.E, -x)));
	}

	/**
	 * Convert degrees to radians
	 */
	public static float radians(float deg) {
		return deg * 0.01745329f;
	}

	/**
	 * Interpolate between value A and B, by the amount delta
	 */
	public static float lerp(float a, float b, float delta){
		return a + delta * (b - a);
	}

	/**
	 * Returns the trigonometric sine of an angle in radians.
	 */
	public static float sin(float value) {
		return (float) Math.sin(value);
	}

	/**
	 * Returns the trigonometric cosine of an angle in radians.
	 */
	public static float cos(float value) {
		return (float) Math.cos(value);
	}

}
