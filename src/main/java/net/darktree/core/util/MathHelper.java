package net.darktree.core.util;

import java.util.Random;

public class MathHelper {

	public static Random RANDOM = new Random();

	public static int nextRandomDice() {
		return RANDOM.nextInt(1, 7);
	}

	public static <T extends Enum<T>> T getRandomEnum(Class<T> enumerable) {
		T[] values = enumerable.getEnumConstants();
		return (T) values[RANDOM.nextInt(values.length)];
	}

}
