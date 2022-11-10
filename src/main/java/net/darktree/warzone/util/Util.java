package net.darktree.warzone.util;

import com.google.common.collect.ImmutableMap;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Util {

	/**
	 * Loads the class and calls the static initializer
	 * (if the class is not yet loaded), based on this <a href="https://stackoverflow.com/a/9391517">Stackoverflow answer</a>
	 */
	public static <T> void load(Class<T> clazz) {
		try {
			Class.forName(clazz.getName(), true, clazz.getClassLoader());
		} catch (ClassNotFoundException e) {
			throw new AssertionError(e);
		}
	}

	/**
	 * Similar to the standard removeIf() but calls the
	 * consumer for each removed element.
	 */
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

	/**
	 * Create an immutable map where every key is a enum value and values are
	 * created with the given function.
	 */
	public static <T extends Enum<T>, V> ImmutableMap<T, V> enumMapOf(Class<T> clazz, Function<T, V> supplier) {
		return ImmutableMap.copyOf(Stream.of(clazz.getEnumConstants()).collect(Collectors.toMap(Function.identity(), supplier)));
	}

}
