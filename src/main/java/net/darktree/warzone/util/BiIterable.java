package net.darktree.warzone.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public final class BiIterable<T> implements Iterable<T> {

	private static final class ReverseIterator<T> implements Iterator<T> {

		private final ListIterator<T> iterator;

		private ReverseIterator(ListIterator<T> iterator) {
			this.iterator = iterator;
		}

		@Override
		public boolean hasNext() {
			return iterator.hasPrevious();
		}

		@Override
		public T next() {
			return iterator.previous();
		}

		@Override
		public void remove() {
			iterator.remove();
		}

	}

	private final List<T> list;
	private final boolean reversed;

	private BiIterable(List<T> list, boolean reversed) {
		this.list = list;
		this.reversed = reversed;
	}

	/**
	 * Create a list iterator of specified direction
	 */
	public static <T> BiIterable<T> of(List<T> list, boolean reversed) {
		return new BiIterable<>(list, reversed);
	}

	/**
	 * Create a forward list iterator
	 */
	public static <T> BiIterable<T> forward(List<T> list) {
		return of(list, false);
	}

	/**
	 * Create a reversed list iterator
	 */
	public static <T> BiIterable<T> reversed(List<T> list) {
		return of(list, true);
	}

	@NotNull
	@Override
	public Iterator<T> iterator() {
		return reversed ? new ReverseIterator<>(list.listIterator(list.size())) : list.iterator();
	}

}
