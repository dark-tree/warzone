package net.darktree.warzone.world.entity.building;

import net.darktree.warzone.Registries;
import net.darktree.warzone.client.render.image.Sprite;
import net.darktree.warzone.client.render.vertex.Renderer;
import net.darktree.warzone.client.render.vertex.VertexBuffer;
import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.country.upgrade.Upgrades;
import net.darktree.warzone.screen.PlayScreen;
import net.darktree.warzone.screen.interactor.BuildInteractor;
import net.darktree.warzone.screen.interactor.PlacementInteractor;
import net.darktree.warzone.util.Direction;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.action.DeconstructBuildingAction;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.entity.StructureEntity;
import net.darktree.warzone.world.pattern.Pattern;
import net.darktree.warzone.world.tile.TilePos;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class Building extends StructureEntity {

	public Building(World world, int x, int y, Building.Type type) {
		super(world, x, y, type.width, type.height, type);
	}

	public static Building from(int x, int y, World world, @NotNull CompoundTag tag) {
		Building building = (Building) Registries.ENTITIES.byKey(tag.getString("id")).value().create(world, x, y);
		building.fromNbt(tag);
		building.getOwner().ifPresent(owner -> owner.addBuilding(building));
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

	public Sprite getSprite() {
		return getType().sprite;
	}

	public Direction getRotation() {
		return Direction.NORTH;
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
		Renderer.quad(buffer, getRotation(), tx, ty, width, height, getSprite(), 0, 0, 0, 0);
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
		getOwner().ifPresent(country -> country.addBuilding(this));
		forEachTile(pos -> world.getTileState(pos).setEntity(this));
		world.getView().markBuildingsDirty();
	}

	@Override
	public void onRemoved() {
		getOwner().ifPresent(country -> country.removeBuilding(this));
		forEachTile(pos -> world.getTileState(pos).removeEntity(this));
		world.getView().markBuildingsDirty();
	}

	public Optional<Country> getOwner() {
		return Optional.ofNullable(world.getCountry(tx, ty));
	}

	public void setFacing(Direction facing) {
		// if the building can be rotated set that rotation here
	}

	public static int remainder(Country country, int materials) {
		return (int) Math.floor(materials * country.upgrades.get(Upgrades.RECYCLE));
	}

	public static Type.Builder define(Entity.Type.Constructor constructor, Sprite sprite) {
		return new Type.Builder(constructor, sprite);
	}

	public static class Type extends Entity.Type {

		public final int value;
		public final int width, height;
		public final Sprite sprite;
		public final Pattern pattern;
		public final PlacementInteractor.Provider interactor;

		public Type(Constructor constructor, int value, int width, int height, Sprite icon, Sprite sprite, PlacementInteractor.Provider interactor) {
			super(constructor, icon);

			this.value = value;
			this.width = width;
			this.height = height;
			this.sprite = sprite;
			this.pattern = Pattern.build(width, height);
			this.interactor = interactor;
		}

		public String getNameKey() {
			return "building." + key() + ".name";
		}

		public String getDescriptionKey() {
			return "building." + key() + ".description";
		}

		public void interact(World world, boolean play) {
			PlayScreen.setInteractor(interactor.create(this, world, play));
		}

		public static class Builder {

			private final Constructor constructor;
			private final Sprite sprite;

			private int value = 0;
			private int width = 1;
			private int height = 1;
			private Sprite icon;
			private PlacementInteractor.Provider interactor;

			Builder(Constructor constructor, Sprite sprite) {
				this.constructor = constructor;
				this.sprite = sprite;
				this.icon = sprite;
				this.interactor = BuildInteractor::new;
			}

			public Builder cost(int value) {
				this.value = value;
				return this;
			}

			public Builder size(int width, int height) {
				this.width = width;
				this.height = height;
				return this;
			}

			public Builder icon(Sprite icon) {
				this.icon = icon;
				return this;
			}

			public Builder interactor(PlacementInteractor.Provider interactor) {
				this.interactor = interactor;
				return this;
			}

			public Type build() {
				return new Type(constructor, value, width, height, icon, sprite, interactor);
			}

		}

	}

}
