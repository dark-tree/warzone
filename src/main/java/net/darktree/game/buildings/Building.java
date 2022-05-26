package net.darktree.game.buildings;

import net.darktree.core.Registries;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.event.ClickEvent;
import net.darktree.core.util.NbtSerializable;
import net.darktree.core.util.Type;
import net.darktree.core.world.Pattern;
import net.darktree.core.world.World;
import net.darktree.core.world.WorldComponent;
import net.darktree.core.world.tile.TileInstance;
import net.darktree.core.world.tile.TileState;
import net.darktree.game.country.Symbol;
import net.darktree.game.tiles.Tiles;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public abstract class Building implements NbtSerializable, WorldComponent {

	protected final int x, y;
	protected final World world;
	protected final Type<Building> type;

	public Building(World world, int x, int y, Type<Building> type) {
		this.x = x;
		this.y = y;
		this.world = world;
		this.type = type;
	}

	public static Building from(int x, int y, World world, @NotNull CompoundTag tag) {
		Building building = Registries.BUILDINGS.getElement(tag.getString("id")).construct(world, x, y);
		building.fromNbt(tag);
		world.setLinkedBuildingAt(x, y, building);
		world.getCountry(world.getTileState(x, y).getOwner()).addBuilding(building);
		return building;
	}

	@Override
	public void toNbt(@NotNull CompoundTag tag) {
		tag.putString("id", Registries.BUILDINGS.keyOf(this.type));
	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {

	}

	@Override
	public boolean canPathfindThrough(World world, int x, int y) {
		return false;
	}

	@Override
	public void onInteract(World world, int x, int y, ClickEvent event) {

	}

	public Pattern getPattern() {
		return Pattern.SQUARE;
	}

	public boolean canAbolish() {
		return true;
	}

	public void draw(int x, int y, VertexBuffer buffer) {

	}

	@Override
	public void onOwnerUpdate(World world, int x, int y, Symbol previous, Symbol current) {
		getPattern().iterate(world, this.x, this.y, pos -> {
			world.getTileState(pos.x, pos.y).setOwner(world, pos.x, pos.y, current);
		});

		world.getCountry(previous).removeBuilding(this);
		world.getCountry(current).addBuilding(this);
	}

	public void removed() {
		getPattern().list(world, x, y, true).forEach(pos -> {
			TileState state = world.getTileState(pos);
			state.setVariant(world, x, y, Tiles.EMPTY.getDefaultVariant(), true);
		});

		world.setLinkedBuildingAt(x, y, null);
		world.getCountry(world.getTileState(x, y).getOwner()).removeBuilding(this);
	}

	public abstract int getCost();

	public int getProfit() {
		return (int) (0.4f * getCost());
	}

	public int getStored() {
		return 0;
	}

	public static class Link extends TileInstance {

		private int bx, by;
		private Building building;

		public Link(World world, int x, int y) {
			super(world, x, y);
		}

		public void linkWith(int bx, int by) {
			this.bx = bx;
			this.by = by;
		}

		public boolean isOrigin() {
			return bx == x && by == y;
		}

		@Override
		public void toNbt(@NotNull CompoundTag tag) {
			tag.putInt("bx", bx);
			tag.putInt("by", by);

			if (isOrigin()) {
				building.toNbt(tag);
			}
		}

		@Override
		public void fromNbt(@NotNull CompoundTag tag) {
			bx = tag.getInt("bx");
			by = tag.getInt("by");

			if (isOrigin()) {
				building = Building.from(bx, by, world, tag);
			}
		}

		public Building getBuilding() {
			if (building == null) {
				building = world.getLinkedBuildingAt(bx, by);
			}

			return building;
		}

	}

}
