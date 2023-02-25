package net.darktree.warzone.country.storage;

import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Resource;

public abstract class Storage {

	public static final int LARGE = 100;
	public static final int SMALL = 10;

	/**
	 * Creates a simple limited storage local to the player
	 * @apiNote Use as a method reference given to the constructor of {@link Resource} and its subclasses
	 */
	public static Storage simple(Resource resource, Country country) {
		return new LocalStorage(LARGE);
	}

	/**
	 * Creates a storage distributed among the buildings owned by the player
	 * @apiNote Use as a method reference given to the constructor of {@link Resource} and its subclasses
	 */
	public static Storage distributed(Resource resource, Country country) {
		return new DistributedStorage(resource, country::getStorageNodes);
	}

	public abstract int getMaxValue();
	public abstract int get();
	public abstract void set(int amount);
	public abstract void add(int amount);

	public final void take(int amount) {
		add(-amount);
	}

	public final void inc() {
		add(+1);
	}

	public final void dec() {
		add(-1);
	}

	public final boolean has(int amount) {
		return get() >= amount;
	}

	public boolean serialize() {
		return true;
	}

}
