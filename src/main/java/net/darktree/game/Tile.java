package net.darktree.game;

import net.darktree.game.nbt.NbtSerializable;
import net.darktree.opengl.vertex.Renderer;
import net.darktree.opengl.vertex.VertexBuffer;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class Tile implements NbtSerializable {

	protected final Type type;
	protected final World world;
	protected final int x, y;

	public Tile(Type type, World world, @Nullable CompoundTag tag, int x, int y) {
		this.type = type;
		this.world = world;
		this.x = x;
		this.y = y;

		this.fromNbt(tag == null ? new CompoundTag() : tag);
	}

	@Override
	public void toNbt(@NotNull CompoundTag tag) {

	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {

	}

	public void draw(VertexBuffer buffer, float x, float y) {
		Renderer.quad(buffer, x, y, 1, 1, this.world.EMPTY);
	}

	public void onInteract(int mode) {

	}

	public void onRemoved(Tile.Type type) {

	}

	/**
	 * a factory unique for every tile class, commonly refers to the constructor
 	 */
	public interface Type {
		Tile create(Type type, World world, @Nullable CompoundTag tag, int x, int y);
	}

}
