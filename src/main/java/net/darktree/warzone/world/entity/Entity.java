package net.darktree.warzone.world.entity;

import net.darktree.warzone.Registries;
import net.darktree.warzone.client.render.WorldBuffers;
import net.darktree.warzone.country.Symbol;
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

	@Override
	public void onAdded() {
		removed = false;
		world.getTileState(tx, ty).setEntity(this);
	}

	public final void remove() {
		removed = true;
	}

	public boolean isRemoved() {
		return removed;
	}

	@Override
	public void onRemoved() {
		world.getTileState(tx, ty).removeEntity(this);
	}

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

	abstract public void draw(WorldBuffers buffers, boolean updateStaticElements);

	public boolean canPathfindThrough(Symbol symbol) {
		return false;
	}

	@Override
	public void toNbt(@NotNull CompoundTag tag) {
		tag.putString("id", type.key());
		tag.putInt("x", getX());
		tag.putInt("y", getY());
	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {

	}

	public static Entity load(World world, @NotNull CompoundTag tag) {
		Entity entity = Registries.ENTITIES.getElement(tag.getString("id")).create(world, tag.getInt("x"), tag.getInt("y"));
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
