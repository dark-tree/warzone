package net.darktree.game.country;

import net.darktree.lt2d.util.NbtSerializable;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class TileOwner implements NbtSerializable {
	private Symbol symbol;
	private boolean control;

	public TileOwner(Symbol symbol, boolean controlled) {
		this.symbol = symbol;
		this.control = controlled;
	}

	public TileOwner() {
		this(Symbol.NONE, false);
	}

	@Override
	public void toNbt(@NotNull CompoundTag tag) {
		tag.putByte("symbol", (byte) this.symbol.ordinal());
		tag.putBoolean("owner", this.control);
	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {
		this.symbol = Symbol.values()[tag.getByte("symbol")];
		this.control = tag.getBoolean("owner");
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	public Symbol getSymbol() {
		return symbol;
	}

	public void setControl(boolean control) {
		this.control = control;
	}

	public Country getCountry() {
		return Country.of(this.symbol);
	}

	/**
	 * Test if that tile is controlled by any country
	 */
	public boolean isControlled() {
		return control;
	}

	/**
	 * Test if that tile is controlled by the given country
	 */
	public boolean isControlled(Symbol symbol) {
		return control && this.symbol == symbol;
	}
}
