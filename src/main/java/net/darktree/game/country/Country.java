package net.darktree.game.country;

import net.darktree.core.util.NbtSerializable;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class Country implements NbtSerializable {
	private final Symbol symbol;
	private int local = 0;

	public Country(Symbol symbol) {
		this.symbol = symbol;
	}

	@Override
	public void toNbt(@NotNull CompoundTag tag) {
		tag.putByte("symbol", (byte) symbol.ordinal());
	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {

	}

	public int getTotalMaterials() {
		return local;
	}

	public void addMaterials(int amount) {
		local += amount;
	}
}
