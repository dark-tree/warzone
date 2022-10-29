package net.darktree.core.util;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Util {

	// https://stackoverflow.com/a/9391517
	public static <T> void load(Class<T> clazz) {
		try {
			Class.forName(clazz.getName(), true, clazz.getClassLoader());
		} catch (ClassNotFoundException e) {
			throw new AssertionError(e);
		}
	}

	public static <T> void consumeIf(List<T> list, Predicate<T> predicate, Consumer<T> consumer) {
		Iterator<T> iterator = list.iterator();

		while (iterator.hasNext()) {
			T element = iterator.next();

			if (predicate.test(element)) {
				consumer.accept(element);
				iterator.remove();
			}
		}
	}

}
