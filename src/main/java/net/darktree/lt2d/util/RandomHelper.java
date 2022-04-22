package net.darktree.lt2d.util;

import java.util.Random;

public class RandomHelper {

	public static Random RANDOM = new Random();

	public static int nextCubeDigit() {
		return RANDOM.nextInt(1, 7);
	}

}
