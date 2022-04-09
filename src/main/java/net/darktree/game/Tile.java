package net.darktree.game;

import net.darktree.game.state.TileState;
import net.darktree.opengl.vertex.Renderer;
import net.darktree.opengl.vertex.VertexBuffer;

public abstract class Tile {
	public final TileState state;

	public Tile() {
		this.state = createDefaultState();
	}

	protected TileState createDefaultState() {
		return TileState.createOf(this);
	}

	public TileState getDefaultState() {
		return this.state;
	}

	public void draw(World world, int x, int y, VertexBuffer buffer) {
		Renderer.quad(buffer, x, y, 1, 1, World.EMPTY);
	}

	public void onInteract(World world, int x, int y, int mode) {

	}

	public void onRemoved(World world, int x, int y, TileState tile) {

	}

}
