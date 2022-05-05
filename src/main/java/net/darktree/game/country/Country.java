package net.darktree.game.country;

import net.darktree.lt2d.util.NbtSerializable;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class Country implements NbtSerializable {
	private final Symbol symbol;

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
}
