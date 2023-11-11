package net.darktree.warzone.util.iterable;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

public class PagedIterable<T> implements Iterable<T> {

	private final List<T> list;
	private final int page;
	private final int size;

	private PagedIterable(List<T> list, int page, int size) {
		this.list = list;
		this.page = page;
		this.size = size;
	}

	/**
	 * Create an iterator that will iterate over subsections of the given size
	 */
	public static <T> PagedIterable<T> of(List<T> list, int page, int size) {
		return new PagedIterable<>(list, page, size);
	}

	@NotNull
	@Override
	public Iterator<T> iterator() {
		int start = Math.max(0, page * size);
		int end = Math.min(list.size(), (page + 1) * size);

		return list.subList(start, end).iterator();
	}

}
