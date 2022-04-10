package net.darktree.lt2d.world;

import net.darktree.lt2d.graphics.vertex.Renderer;
import net.darktree.lt2d.graphics.vertex.VertexBuffer;
import net.darktree.lt2d.world.state.TileState;
import org.jetbrains.annotations.Nullable;

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

	public void onRemoved(World world, int x, int y, TileState state) {

	}

	/**
	 * Return the Tile Instance for this tile, or null if there should be no tile instance
	 */
	public @Nullable TileInstance getInstance(World world, int x, int y) {
		return null;
	}

}
