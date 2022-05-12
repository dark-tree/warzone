package net.darktree.core.util;

import java.util.Random;

public class RandomHelper {

	public static Random RANDOM = new Random();

	public static int nextCubeDigit() {
		return RANDOM.nextInt(1, 7);
	}

	public static <T extends Enum<T>> T getRandomEnum(Class<T> enumerable) {
		T[] values = enumerable.getEnumConstants();
		return (T) values[RANDOM.nextInt(values.length)];
	}

}
