package net.darktree.warzone.world.tile;

import net.darktree.warzone.Registries;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.NbtSerializable;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.tile.tiles.Tiles;
import net.darktree.warzone.world.tile.variant.TileVariant;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

final public class TileState implements NbtSerializable {

	private TileVariant variant;
	private Symbol owner;
	private Entity entity;

	public TileState(TileVariant variant, Symbol owner) {
		this.variant = variant;
		this.owner = owner;
	}

	@Override
	public void toNbt(@NotNull CompoundTag tag) {
		variant.toNbt(tag);
		tag.putByte("owner", (byte) owner.ordinal());
		tag.putString("id", variant.getTile().key());
	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {
		variant = Registries.TILES.byKey(tag.getString("id")).value().getDefaultVariant().fromNbt(tag);
		owner = Symbol.values()[tag.getByte("owner")];
	}

	public void load(World world, int x, int y, CompoundTag tag) {
		try {
			CompoundTag tile = tag.getCompoundTag(x + " " + y);
			fromNbt(tile);
		}catch (Exception e) {
			Logger.warn("Loading of tile at: (", x, ", ", y, ") failed! Reverting to default...");
			world.setTileVariant(x, y, Tiles.EMPTY.getDefaultVariant());
			owner = Symbol.NONE;
		}
	}

	/**
	 * Set the tile variant for this tile state
	 */
	public void setVariant(World world, TileVariant variant) {
		this.variant = variant;
		world.getView().markSurfaceDirty();
	}

	/**
	 * Set the tile owner for this tile state
	 */
	public void setOwner(World world, Symbol owner, boolean notify) {
		if (notify && this.owner != owner && entity != null) {
			entity.onOwnerUpdate(this.owner, owner);
		}

		this.owner = owner;
		world.onOwnershipChanged();
	}

	/**
	 * Get the tile from this tile state
	 */
	public Tile getTile() {
		return this.variant.getTile();
	}

	/**
	 * Get the tile variant from this tile state
	 */
	public TileVariant getVariant() {
		return variant;
	}

	/**
	 * Get the tile owner from this tile state
	 */
	public Symbol getOwner() {
		return this.owner;
	}

	/**
	 * Get the entity (or null) from this tile state
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * Place an entity on this tile.<br>
	 * <b>For internal use only!</b>
	 */
	public void setEntity(Entity entity) {
		if (this.entity == null) this.entity = entity;
	}

	/**
	 * Remove an entity from this tile.<br>
	 * <b>For internal use only!</b>
	 */
	public void removeEntity(Entity entity) {
		if (this.entity == entity) this.entity = null;
	}

}
