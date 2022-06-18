package net.darktree.game.buildings;

import net.darktree.core.Registries;
import net.darktree.core.client.Colors;
import net.darktree.core.client.render.color.Color;
import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.event.ClickEvent;
import net.darktree.core.util.BuildingType;
import net.darktree.core.util.NbtSerializable;
import net.darktree.core.world.Pattern;
import net.darktree.core.world.World;
import net.darktree.core.world.WorldComponent;
import net.darktree.core.world.action.DeconstructBuildingAction;
import net.darktree.core.world.overlay.Overlay;
import net.darktree.core.world.tile.TileInstance;
import net.darktree.game.country.Symbol;
import net.darktree.game.tiles.Tiles;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class Building implements NbtSerializable, WorldComponent {

	protected final World world;
	public final BuildingType type;
	public final int x, y;

	public Building(World world, int x, int y, BuildingType type) {
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
		return type.pattern;
	}

	@Override
	public boolean isDeconstructable(World world, int x, int y) {
		return true;
	}

	@Override
	public void deconstruct(World world, int x, int y) {
		world.getManager().apply(new DeconstructBuildingAction(this, this.x, this.y));
	}

	public void draw(int x, int y, VertexBuffer buffer) {
		Overlay overlay = world.getOverlay();
		Color c = overlay == null ? Colors.OVERLAY_NONE : overlay.getColor(world, x, y, world.getTileState(x, y));
		Renderer.quad(buffer, x, y, 2, 2, type.sprite, c.r, c.g, c.b, c.a);
	}

	@Override
	public void onOwnerUpdate(World world, int x, int y, Symbol previous, Symbol current) {
		getPattern().iterate(world, this.x, this.y, pos -> {
			world.getTileState(pos).setOwner(world, pos.x, pos.y, current, false);
		});

		world.getCountry(previous).removeBuilding(this);
		world.getCountry(current).addBuilding(this);
	}

	public void remove() {
		getPattern().list(world, x, y, true).forEach(pos -> {
			world.setTileVariant(pos.x, pos.y, Tiles.EMPTY.getDefaultVariant());
		});

		world.setLinkedBuildingAt(x, y, null);
		world.getCountry(world.getTileState(x, y).getOwner()).removeBuilding(this);
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
