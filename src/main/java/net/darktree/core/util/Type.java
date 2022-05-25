package net.darktree.core.util;

import net.darktree.core.world.World;

/**
 * Generic class used for identifying some registry items,
 * like buildings and entities.
 */
public class Type<T> {
	private final Constructor<T> constructor;

	public Type(Constructor<T> constructor) {
		this.constructor = constructor;
	}

	public T construct(World world, int x, int y) {
		return constructor.construct(world, x, y, this);
	}

	public interface Constructor<T> {
		T construct(World world, int x, int y, Type<T> type);
	}
}