package net.darktree.game;

import net.darktree.game.nbt.NbtSerializable;
import net.darktree.opengl.vertex.Renderer;
import net.darktree.opengl.vertex.VertexBuffer;
import net.querz.nbt.tag.CompoundTag;

public abstract class Tile implements NbtSerializable {

	protected final World world;
	protected final int x, y;

	public Tile(World world, int x, int y) {
		this.world = world;
		this.x = x;
		this.y = y;
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
	}

	public void interact(int mode) {

	}

	public interface Factory {
		Tile create(World world, CompoundTag tag, int x, int y);
	}

}
