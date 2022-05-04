package net.darktree.lt2d.world;

import net.darktree.game.country.Symbol;
import net.darktree.game.country.TileOwner;
import net.darktree.game.tiles.Tiles;
import net.darktree.lt2d.Registries;
import net.darktree.lt2d.util.Direction;
import net.darktree.lt2d.util.Logger;
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
			CompoundTag data = new CompoundTag();
			instance.toNbt(data);
			tag.put("data", data);
		}
	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {
		variant = Registries.TILES.getElement(tag.getString("id")).getDefaultVariant().fromNbt(tag);
		owner = new TileOwner(tag);
	}

	public void load(World world, int x, int y, CompoundTag tag) {
		try {
			CompoundTag tile = tag.getCompoundTag(x + " " + y);
			fromNbt(tile);
			instance = variant.getTile().getInstance(world, x, y);

			if (instance != null) {
				instance.fromNbt(tile.getCompoundTag("data"));
			}
		}catch (Exception e) {
			Logger.warn("Loading of tile at: ", x, " ", y, " failed! Reverting to default...");

			// TODO make better
			setVariant(world, x, y, Tiles.EMPTY.getDefaultVariant(), false);
			owner = new TileOwner();
		}
	}

	public void setVariant(World world, int x, int y, TileVariant variant, boolean notify) {
		if (this.variant != null) {
			this.variant.getTile().onRemoved(world, x, y, variant);
		}

		this.variant = variant;
		this.instance = variant.getTile().getInstance(world, x, y);

		if(notify) {
			for (Direction direction : Direction.values()) {
				int nx = x + direction.x;
				int ny = y + direction.y;

				if (world.isPositionValid(nx, ny)) {
					world.getTileState(nx, ny).getTile().onNeighbourUpdate(world, x, y, direction);
				}
			}
		}
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

	public void setOwner(World world, int x, int y, TileOwner owner) {
		if (!this.owner.equals(owner)) {
			this.owner = owner;
			getTile().onOwnerUpdate(world, x, y, this.owner, owner);
		}
	}

	public TileInstance getInstance() {
		return instance;
	}
}
