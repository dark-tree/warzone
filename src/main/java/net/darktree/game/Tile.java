package net.darktree.game;

import net.darktree.game.nbt.NbtSerializable;
import net.darktree.opengl.vertex.Renderer;
import net.darktree.opengl.vertex.VertexBuffer;
import net.querz.nbt.tag.CompoundTag;

public abstract class Tile implements NbtSerializable {

	protected final World world;

	public Tile(World world) {
		this.world = world;
	}

	@Override
	public void toNbt(CompoundTag tag) {
		tag.putInt("id", Registry.getId(getClass()));
	}

	@Override
	public void fromNbt(CompoundTag tag) {

	}

	public void draw(VertexBuffer buffer, float x, float y, float scale) {
		Renderer.quad(buffer, x, y, scale, scale, this.world.EMPTY);
		Renderer.quad(buffer, x, y, scale, scale, this.world.CIRCLE);
	}

	public interface Factory {
		Tile create(World world, CompoundTag tag);
	}

}
