package net.darktree.game.country;

import net.darktree.lt2d.util.NbtSerializable;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import javax.naming.OperationNotSupportedException;
import java.util.Objects;

public class TileOwner implements NbtSerializable {
	public final Symbol symbol;
	public final boolean control;

	public TileOwner(Symbol symbol, boolean controlled) {
		this.symbol = symbol;
		this.control = controlled;
	}

	public TileOwner(@NotNull CompoundTag tag) {
		this.symbol = Symbol.values()[tag.getByte("symbol")];
		this.control = tag.getBoolean("owner");
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
		throw new RuntimeException("Unable to load from tag after init!");
	}

	public Country getCountry() {
		return Country.of(this.symbol);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;

		if (object instanceof TileOwner owner) {
			return control == owner.control && symbol == owner.symbol;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(symbol, control);
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
