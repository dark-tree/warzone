package net.darktree.lt2d.world;

import net.darktree.lt2d.Registries;
import net.darktree.lt2d.graphics.vertex.Renderer;
import net.darktree.lt2d.graphics.vertex.VertexBuffer;
import net.darktree.lt2d.world.state.TileState;
import org.jetbrains.annotations.Nullable;

public abstract class Tile {
	public final TileState state;
	protected String name = null;

	public Tile() {
		this.state = createDefaultState();
	}

	protected TileState createDefaultState() {
		return TileState.createOf(this);
	}

	public TileState getDefaultState() {
		return this.state;
	}

	public void draw(int x, int y, VertexBuffer buffer) {
		Renderer.tile(buffer, x, y, Registries.TILE_SPRITES.get(this.name));
	}

	public void onInteract(World world, int x, int y, int mode) {

	}

	public void onRemoved(World world, int x, int y, TileState state) {

	}

	public void setName(String name) {
		if (this.name == null) {
			this.name = name;
		}else{
			throw new RuntimeException("Tile name already set!");
		}
	}

	public boolean canPathfindThrough() {
		return true;
	}

	/**
	 * Return the Tile Instance for this tile, or null if there should be no tile instance
	 */
	public @Nullable TileInstance getInstance(World world, int x, int y) {
		return null;
	}
}
