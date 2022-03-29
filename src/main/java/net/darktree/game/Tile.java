package net.darktree.game;

import net.darktree.game.nbt.NbtSerializable;
import net.darktree.opengl.image.Atlas;
import net.darktree.opengl.vertex.Renderer;
import net.darktree.opengl.vertex.VertexBuffer;
import net.querz.nbt.tag.CompoundTag;

public abstract class Tile implements NbtSerializable {

	protected final Tile.Factory factory;
	protected final World world;
	protected final int x, y;

	public Tile(Tile.Factory factory, World world, int x, int y) {
		this.factory = factory;
		this.world = world;
		this.x = x;
		this.y = y;
	}

	@Override
	public void toNbt(CompoundTag tag) {
		tag.putInt("id", Registries.TILES.getIdentifier(factory));
	}

	@Override
	public void fromNbt(CompoundTag tag) {

	}

	public void draw(VertexBuffer buffer, float x, float y) {
		Renderer.quad(buffer, x, y, 1, 1, this.world.EMPTY);
	}

	public void interact(int mode) {

	}

	public interface Factory {
		Tile create(Tile.Factory factory, World world, CompoundTag tag, int x, int y);
		void requestSprites(Atlas atlas);
	}

}
