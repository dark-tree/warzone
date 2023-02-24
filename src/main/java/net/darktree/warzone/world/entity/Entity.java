package net.darktree.warzone.world.entity;

import net.darktree.warzone.Registries;
import net.darktree.warzone.client.render.WorldBuffers;
import net.darktree.warzone.util.ElementType;
import net.darktree.warzone.util.NbtSerializable;
import net.darktree.warzone.util.Registry;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.WorldComponent;
import net.darktree.warzone.world.WorldListener;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public abstract class Entity implements NbtSerializable, WorldListener, WorldComponent {

	protected final World world;
	protected int tx, ty;
	protected final Type type;
	protected boolean removed = false;

	public Entity(World world, int x, int y, Type type) {
		this.world = world;
		this.type = type;

		setPos(x, y);
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

	public int getX() {
		return tx;
	}

	public int getY() {
		return ty;
	}

	protected void setPos(int x, int y) {
		this.tx = x;
		this.ty = y;
	}

	@Override
	public void onAdded() {
		removed = false;
		world.getTileState(tx, ty).setEntity(this);
		world.markOverlayDirty();
	}

	@Override
	public void onRemoved() {
		world.getTileState(tx, ty).removeEntity(this);
		world.markOverlayDirty();
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
	public static Entity load(World world, @NotNull CompoundTag tag) {
		Entity entity = Registries.ENTITIES.byKey(tag.getString("id")).value().create(world, tag.getInt("x"), tag.getInt("y"));
		entity.fromNbt(tag);
		return entity;
	}

	public static class Type extends ElementType<Entity.Type> {
		private final Constructor constructor;

		public Type(Constructor constructor) {
			this.constructor = constructor;
		}

		public Entity create(World world, int x, int y) {
			return constructor.create(world, x, y);
		}

		@Override
		public Registry<Type> getRegistry() {
			return Registries.ENTITIES;
		}

		public interface Constructor {
			Entity create(World world, int x, int y);
		}
	}

}
