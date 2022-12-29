package net.darktree.warzone.country.storage;

import net.darktree.warzone.util.math.MathHelper;

public class LocalStorage extends Storage {

	private int value;
	private final int limit;

	public LocalStorage(int limit) {
		this.limit = limit;
	}

	@Override
	public int getMaxValue() {
		return limit;
	}

	@Override
	public int get() {
		return value;
	}

	@Override
	public void set(int amount) {
		this.value = MathHelper.clamp(amount, 0, getMaxValue());
	}

	@Override
	public void add(int amount) {
		set(this.value + amount);
	}

}
