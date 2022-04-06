package net.darktree.game.state;

import java.util.Arrays;

public abstract class Property<T> {

	private final T fallback;

	protected Property(T fallback) {
		this.fallback = fallback;
	}

	public abstract String name();
	public abstract T[] values();

	public int indexOf(Object object) {
		int i = Arrays.asList(values()).indexOf(object);

		if (i == -1) {
			throw new RuntimeException("Value does not belong to this property!");
		}

		return i;
	}

	public T getDefault() {
		return this.fallback;
	}

}
