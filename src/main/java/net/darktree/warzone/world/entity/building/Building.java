package net.darktree.warzone.world.entity.building;

import net.darktree.warzone.Registries;
import net.darktree.warzone.client.render.image.Sprite;
import net.darktree.warzone.client.render.vertex.Renderer;
import net.darktree.warzone.client.render.vertex.VertexBuffer;
import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.action.DeconstructBuildingAction;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.entity.StructureEntity;
import net.darktree.warzone.world.pattern.Pattern;
import net.darktree.warzone.world.tile.TilePos;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public abstract class Building extends StructureEntity {

	public Building(World world, int x, int y, Building.Type type) {
		super(world, x, y, type.width, type.height, type);
	}

	public static Building from(int x, int y, World world, @NotNull CompoundTag tag) {
		Building building = (Building) Registries.ENTITIES.byKey(tag.getString("id")).value().create(world, x, y);
		building.fromNbt(tag);
		building.getOwner().addBuilding(building);
		return building;
	}

	public final Building.Type getType() {
		return (Building.Type) type;
	}

	public final void forEachTile(Consumer<TilePos> consumer) {
		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				consumer.accept(new TilePos(x + tx, y + ty));
			}
		}
	}

	public final Sprite getSprite() {
		return getType().sprite;
	}

	@Override
	public boolean isDeconstructable() {
		return true;
	}

	@Override
	public void deconstruct() {
		this.world.getManager().apply(new DeconstructBuildingAction(world, getX(), getY()));
	}

	protected void draw(VertexBuffer buffer) {
		Renderer.quad(buffer, tx, ty, width, height, getSprite(), 0, 0, 0, 0);
	}

	@Override
	public void onOwnerUpdate(Symbol previous, Symbol current) {
		forEachTile(pos -> {
			world.getTileState(pos).setOwner(world, current, false);
		});

		world.getCountry(previous).removeBuilding(this);
		world.getCountry(current).addBuilding(this);
	}

	@Override
	public void onAdded() {
		removed = false;
		getOwner().addBuilding(this);
		forEachTile(pos -> world.getTileState(pos).setEntity(this));
		world.onBuildingChanged();
	}

	@Override
	public void onRemoved() {
		getOwner().removeBuilding(this);
		forEachTile(pos -> world.getTileState(pos).removeEntity(this));
		world.onBuildingChanged();
	}

	public Country getOwner() {
		return world.getCountry(tx, ty);
	}

	public static class Type extends Entity.Type {

		public final int value;
		public final int width, height;
		public final Sprite icon, sprite;
		public final Pattern pattern;

		public Type(Constructor constructor, int value, int width, int height, Sprite icon, Sprite sprite) {
			super(constructor);

			this.value = value;
			this.width = width;
			this.height = height;
			this.icon = icon;
			this.sprite = sprite;
			this.pattern = Pattern.build(width, height);
		}

		public Type(Constructor constructor, int value, int width, int height, Sprite sprite) {
			this(constructor, value, width, height, sprite, sprite);
		}

	}

}
