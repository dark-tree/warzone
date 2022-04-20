package net.darktree.lt2d.world;

import net.darktree.game.country.TileOwner;
import net.darktree.lt2d.Registries;
import net.darktree.lt2d.util.NbtSerializable;
import net.darktree.lt2d.world.state.TileVariant;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

final public class TileState implements NbtSerializable {

	private TileVariant variant;
	private TileInstance instance;
	private TileOwner owner;

	TileState(TileVariant variant, TileInstance instance, TileOwner owner) {
		this.variant = variant;
		this.instance = instance;
		this.owner = owner;
	}

	@Override
	public void toNbt(@NotNull CompoundTag tag) {
		variant.toNbt(tag);
		owner.toNbt(tag);

		tag.putString("id", Registries.TILES.getKey(variant.getTile()));

		if (instance != null) {
			instance.toNbt(tag);
		}
	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {
		variant = Registries.TILES.getElement(tag.getString("id")).getDefaultState().fromNbt(tag);
		owner.fromNbt(tag);
	}

	public void load(World world, int x, int y, CompoundTag tag) {
		CompoundTag tile = tag.getCompoundTag(x + " " + y);
		fromNbt(tile);
		instance = variant.getTile().getInstance(world, x, y);

		if (instance != null) {
			instance.fromNbt(tile);
		}
	}

	public void setVariant(World world, int x, int y, TileVariant variant) {
		if (this.variant != null) {
			this.variant.getTile().onRemoved(world, x, y, variant);
		}

		this.variant = variant;
		this.instance = variant.getTile().getInstance(world, x, y);
	}

	public Tile getTile() {
		return this.variant.getTile();
	}

	public TileVariant getVariant() {
		return variant;
	}

	public TileOwner getOwner() {
		return this.owner;
	}

	public TileInstance getInstance() {
		return instance;
	}
}
