package net.darktree.game.buildings;

import net.darktree.Main;
import net.darktree.lt2d.Registries;
import net.darktree.lt2d.graphics.vertex.VertexBuffer;
import net.darktree.lt2d.util.NbtSerializable;
import net.darktree.lt2d.util.Type;
import net.darktree.lt2d.world.Pattern;
import net.darktree.lt2d.world.TileInstance;
import net.darktree.lt2d.world.World;
import net.darktree.lt2d.world.WorldComponent;
import net.darktree.lt2d.world.state.TileVariant;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class Building implements NbtSerializable, WorldComponent {

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
		world.setBuildingAt(x, y, building);
		return building;
	}

	@Override
	public void toNbt(@NotNull CompoundTag tag) {
		tag.putString("id", Registries.BUILDINGS.getKey(this.type));
	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {

	}

	@Override
	public boolean canPathfindThrough(World world, int x, int y) {
		return false;
	}

	@Override
	public void onInteract(World world, int x, int y, int mode) {

	}

	public Pattern getPattern() {
		return Pattern.SQUARE;
	}

	public void draw(int x, int y, VertexBuffer buffer) {
		// TODO: render something
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
				building = Building.from(bx, by, Main.world, tag);
			}
		}

		public Building getBuilding() {
			if (building == null) {
				building = Main.world.getBuildingAt(bx, by);
			}

			return building;
		}

	}

}
