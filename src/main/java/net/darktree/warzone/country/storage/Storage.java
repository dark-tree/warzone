package net.darktree.warzone.country.storage;

import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Resource;

public abstract class Storage {

	public static Storage simple(Resource resource, Country country) {
		return new LocalStorage(100);
	}

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
