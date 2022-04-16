package net.darktree.lt2d.world.entities;

import net.darktree.lt2d.Registries;
import net.darktree.lt2d.graphics.vertex.VertexBuffer;
import net.darktree.lt2d.util.NbtSerializable;
import net.darktree.lt2d.world.World;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class Entity implements NbtSerializable {

	protected final World world;
	protected final Type<?> type;
	protected int tx, ty;
	protected float x, y;
	public boolean removed = false;

	public Entity(World world, int x, int y, Type<?> type) {
		this.world = world;
		this.type = type;

		setPos(x, y);
	}

	public boolean isAt(int x, int y) {
		return this.tx == x && this.ty == y;
	}

	public void setPos(int x, int y) {
		this.x = this.tx = x;
		this.y = this.ty = y;
	}

	public void draw(VertexBuffer buffer) {

	}

	@Override
	public void toNbt(@NotNull CompoundTag tag) {
		tag.putString("id", Registries.ENTITIES.getKey(this.type));
		tag.putInt("x", tx);
		tag.putInt("y", ty);
	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {

	}

	public static Entity load(World world, @NotNull CompoundTag tag) {
		Entity entity = Registries.ENTITIES.getElement(tag.getString("id")).construct(world, tag.getInt("x"), tag.getInt("y"));
		entity.fromNbt(tag);
		return entity;
	}

	public static class Type<T extends Entity> {
		private final Class<T> clazz;

		public Type(Class<T> clazz) {
			this.clazz = clazz;
		}

		public T construct(World world, int x, int y) {
			try {
				return clazz.getConstructor(World.class, int.class, int.class, Type.class).newInstance(world, x, y, this);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

}
