package net.darktree.game;

import net.darktree.game.nbt.NbtSerializable;
import net.darktree.game.state.Property;
import net.darktree.game.state.TileState;
import net.darktree.opengl.vertex.Renderer;
import net.darktree.opengl.vertex.VertexBuffer;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Tile implements NbtSerializable {

	public final Type type;
	public final World world;
	public final int x, y;
	public final TileState state;

	public Tile(Type type, World world, @Nullable CompoundTag tag, int x, int y) {
		this.type = type;
		this.world = world;
		this.x = x;
		this.y = y;
		this.state = type.state.build();

		this.fromNbt(tag == null ? new CompoundTag() : tag);
	}

	@Override
	public void toNbt(@NotNull CompoundTag tag) {
		this.state.toNbt(tag);
	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {
		this.state.fromNbt(tag);
	}

	public void draw(VertexBuffer buffer, float x, float y) {
		Renderer.quad(buffer, x, y, 1, 1, World.EMPTY);
	}

	public void onInteract(int mode) {

	}

	public void onRemoved(Tile.Type type) {

	}

	/**
	 * a factory unique for every tile class, commonly refers to the constructor
 	 */
	public interface Constructor {
		Tile create(Type type, World world, @Nullable CompoundTag tag, int x, int y);
	}

	public static class Type implements Constructor {
		private final Constructor constructor;
		private final TileState.Builder state;

		public Type(Constructor constructor, TileState.Builder state) {
			this.constructor = constructor;
			this.state = state;
		}

		public static Type of(Constructor constructor, Property<?>... properties) {
			TileState.Builder builder = TileState.create();

			for (Property<?> property : properties) {
				builder.add(property, property.getDefault());
			}

			return new Type(constructor, builder);
		}

		@Override
		public Tile create(Type type, World world, @Nullable CompoundTag tag, int x, int y) {
			return this.constructor.create(this, world, tag, x, y);
		}
	}

}
