package net.darktree.warzone.util;

import com.google.common.collect.ImmutableMap;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
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
	 * Create an immutable map where every key is an enum value and values are
	 * created with the given function.
	 */
	public static <T extends Enum<T>, V> ImmutableMap<T, V> enumMapOf(Class<T> clazz, Function<T, V> supplier) {
		return ImmutableMap.copyOf(Stream.of(clazz.getEnumConstants()).collect(Collectors.toMap(Function.identity(), supplier)));
	}

	/**
	 * Run given lambda on a new thread
	 */
	public static void runAsync(Runnable runnable, String name) {
		Thread thread = new Thread(runnable);
		thread.setName(name);
		thread.setDaemon(true);
		thread.start();
	}

	/**
	 * Run given lambda on a new thread after some time passes
	 */
	public static Timer runAsyncAfter(Runnable runnable, int milliseconds) {
		Timer timer = new Timer();
		timer.schedule(
				new TimerTask() {

					@Override
					public void run() {
						runnable.run();
					}

				},
				milliseconds
		);
		return timer;
	}

	/**
	 * Convert the given registry into a list
	 */
	public static <T extends ElementType<T>> List<T> registryAsList(Registry<T> registry) {
		List<T> list = new ArrayList<>();
		registry.iterate(list::add);
		return list;
	}

	/**
	 * Safely delete a directory
	 */
	public static boolean deleteDirectory(File file, boolean recursive) {
		File[] contents = file.listFiles();

		if (contents != null) {
			for (File child : contents) {
				if (!Files.isSymbolicLink(child.toPath())) {
					if (recursive) {
						deleteDirectory(child, true);
					} else {
						child.delete();
					}
				}
			}
		}

		return file.delete();
	}

	/**
	 * Find min (-1) or max (1) value of a list
	 */
	public static <T, C> T getExtremeValue(List<T> list, Function<T, C> indexer, Comparator<C> comparator, int direction) {
		C index = null;
		T extreme = null;

		for (T entry : list) {
			C current = indexer.apply(entry);

			if (index == null || Integer.signum(comparator.compare(index, current)) == direction) {
				extreme = entry;
				index = current;
			}
		}

		return extreme;
	}

}
