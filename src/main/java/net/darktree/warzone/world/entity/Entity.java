package net.darktree.warzone.world.entity;

import net.darktree.warzone.Registries;
import net.darktree.warzone.client.render.WorldBuffers;
import net.darktree.warzone.client.render.image.Sprite;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.util.ElementType;
import net.darktree.warzone.util.NbtSerializable;
import net.darktree.warzone.util.Registry;
import net.darktree.warzone.world.Update;
import net.darktree.warzone.world.WorldComponent;
import net.darktree.warzone.world.WorldListener;
import net.darktree.warzone.world.WorldSnapshot;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public abstract class Entity implements NbtSerializable, WorldListener, WorldComponent {

	protected final WorldSnapshot world;
	protected int tx, ty;
	protected final Type type;
	protected boolean removed = false;

	public Entity(WorldSnapshot world, int x, int y, Type type) {
		this.world = world;
		this.type = type;
		this.tx = x;
		this.ty = y;
	}

	/**
	 * Called during world rendering, updateStaticElements is set to true only
	 * when the building render layer has been cleared
	 */
	abstract public void draw(WorldBuffers buffers, boolean updateStaticElements);

	/**
	 * Mark this entity for removal
	 */
	public final void remove() {
		removed = true;
	}

	/**
	 * Checks if this entity should be removed
	 */
	public final boolean isRemoved() {
		return removed;
	}

	/**
	 * Checks if this entity is at the given pos
	 */
	public boolean isAt(int x, int y) {
		return getX() == x && getY() == y;
	}

	/**
	 * Check if this entity can be manipulated by player 'symbol'
	 */
	public boolean isInControl(Symbol symbol) {
		return world.canControl(getX(), getY(), symbol);
	}

	/**
	 * Check if this entity belongs to the player of the given symbol
	 */
	public boolean isOf(Symbol symbol) {
		return world.getTileState(getX(), getY()).getOwner() == symbol;
	}

	public int getX() {
		return tx;
	}

	public int getY() {
		return ty;
	}

	@Override
	public void onAdded() {
		removed = false;
		world.getTileState(tx, ty).setEntity(this);
		world.pushUpdateBits(Update.OVERLAY);
	}

	@Override
	public void onRemoved() {
		world.getTileState(tx, ty).removeEntity(this);
		world.pushUpdateBits(Update.OVERLAY);
	}

	@Override
	public void toNbt(@NotNull CompoundTag nbt) {
		nbt.putString("id", type.key());
		nbt.putInt("x", getX());
		nbt.putInt("y", getY());
	}

	@Override
	public void fromNbt(@NotNull CompoundTag nbt) {

	}

	/**
	 * Used for deserializing an entity from NBT data, called during world loading
	 * @return the deserialized entity
	 */
	public static Entity load(WorldSnapshot world, @NotNull CompoundTag tag) {
		Entity entity = Registries.ENTITIES.byKey(tag.getString("id")).value().create(world, tag.getInt("x"), tag.getInt("y"));
		entity.fromNbt(tag);
		return entity;
	}

	public Entity copy(WorldSnapshot copy) {
		return type.create(copy, tx, ty).copyFrom(this);
	}

	public Entity copyFrom(Entity entity) {
		this.removed = entity.removed;
		return this;
	}

	public static class Type extends ElementType<Entity.Type> {
		private final Constructor constructor;
		public final Sprite icon;

		public Type(Constructor constructor, Sprite icon) {
			this.constructor = constructor;
			this.icon = icon;
		}

		public Entity create(WorldSnapshot world, int x, int y) {
			return constructor.create(world, x, y);
		}

		@Override
		public Registry<Type> getRegistry() {
			return Registries.ENTITIES;
		}

		public interface Constructor {
			Entity create(WorldSnapshot world, int x, int y);
		}
	}

}
