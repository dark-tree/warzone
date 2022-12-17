package net.darktree.warzone.world.tile.variant.property;

import java.util.Arrays;

public abstract class Property<T> {

	public abstract String name();
	public abstract T[] values();

	public T getDefault() {
		return values()[0];
	}

	public int indexOf(T object) {
		int i = Arrays.asList(values()).indexOf(object);

		if (i == -1) {
			throw new RuntimeException("Given value does not belong to this property!");
		}

		return i;
	}

}
