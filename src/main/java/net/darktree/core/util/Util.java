package net.darktree.core.util;

public class Util {

	// https://stackoverflow.com/a/9391517
	public static <T> void load(Class<T> clazz) {
		try {
			Class.forName(clazz.getName(), true, clazz.getClassLoader());
		} catch (ClassNotFoundException e) {
			throw new AssertionError(e);
		}
	}

}
