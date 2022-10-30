package net.darktree.core.world.entity;

import net.darktree.core.Registries;
import net.darktree.core.client.render.WorldBuffers;
import net.darktree.core.util.NbtSerializable;
import net.darktree.core.world.World;
import net.darktree.core.world.WorldComponent;
import net.darktree.core.world.WorldListener;
import net.darktree.game.country.Symbol;
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
		world.getTileState(tx, ty).setEntity(this);
	}

	public void remove() {
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
		tag.putString("id", Registries.ENTITIES.keyOf(this.type));
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

	public static class Type {
		private final Constructor constructor;

		public Type(Constructor constructor) {
			this.constructor = constructor;
		}

		public Entity create(World world, int x, int y) {
			return constructor.create(world, x, y);
		}

		public interface Constructor {
			Entity create(World world, int x, int y);
		}
	}

}
