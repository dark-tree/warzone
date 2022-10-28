package net.darktree.core.world.tile;

import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.world.World;
import net.darktree.core.world.WorldTile;
import net.darktree.core.world.tile.variant.TileVariant;
import org.jetbrains.annotations.Nullable;

public abstract class Tile implements WorldTile {

	public final TileVariant variant;

	public Tile() {
		this.variant = createDefaultVariant();
	}

	protected TileVariant createDefaultVariant() {
		return TileVariant.createOf(this);
	}

	public TileVariant getDefaultVariant() {
		return this.variant;
	}

	public void draw(World world, int x, int y, TileState state, VertexBuffer buffer) {
		Renderer.tile(buffer, world, x, y, state, SpriteBridge.getSprite(this));
	}

	/**
	 * Return the Tile Instance for this tile, or null if there should be no tile instance
	 */
	@Deprecated
	public @Nullable TileInstance getInstance(World world, int x, int y) {
		return null;
	}

}
