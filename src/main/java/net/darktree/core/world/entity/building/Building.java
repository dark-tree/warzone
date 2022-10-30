package net.darktree.core.world.entity.building;

import net.darktree.core.Registries;
import net.darktree.core.client.render.image.Sprite;
import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.util.BuildingType;
import net.darktree.core.world.World;
import net.darktree.core.world.action.DeconstructBuildingAction;
import net.darktree.core.world.entity.StructureEntity;
import net.darktree.core.world.tile.TilePos;
import net.darktree.game.country.Symbol;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public abstract class Building extends StructureEntity {

	public Building(World world, int x, int y, BuildingType type) {
		super(world, x, y, type.width, type.height, type);
	}

	public static Building from(int x, int y, World world, @NotNull CompoundTag tag) {
		Building building = (Building) Registries.ENTITIES.getElement(tag.getString("id")).create(world, x, y);
		building.fromNbt(tag);
		world.getCountry(world.getTileState(x, y).getOwner()).addBuilding(building);
		return building;
	}

	public final BuildingType getType() {
		return (BuildingType) type;
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
		this.world.getManager().apply(new DeconstructBuildingAction(this, getX(), getY()));
	}

	public void draw(VertexBuffer buffer) {
		Renderer.quad(buffer, tx, ty, width, height, getSprite(), 0, 0, 0, 0);
	}

	@Override
	public void onOwnerUpdate(Symbol previous, Symbol current) {
		forEachTile(pos -> {
			world.getTileState(pos).setOwner(world, pos.x, pos.y, current, false);
		});

		world.getCountry(previous).removeBuilding(this);
		world.getCountry(current).addBuilding(this);
	}

	@Deprecated
	public int getStored() {
		return 0;
	}

	@Override
	public void onAdded() {
		world.getCountry(tx, ty).addBuilding(this);
		forEachTile(pos -> world.getTileState(pos).setEntity(this));
		world.onBuildingChanged();
	}

	@Override
	public void onRemoved() {
		world.getCountry(tx, ty).removeBuilding(this);
		forEachTile(pos -> world.getTileState(pos).removeEntity(this));
		world.onBuildingChanged();
	}

}
