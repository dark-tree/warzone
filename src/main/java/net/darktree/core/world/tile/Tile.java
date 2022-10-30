package net.darktree.core.world.tile;

import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.world.Surface;
import net.darktree.core.world.WorldTile;
import net.darktree.core.world.tile.variant.TileVariant;

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

	public void draw(int x, int y, VertexBuffer buffer) {
		Renderer.tile(buffer, x, y, SpriteBridge.getSprite(this));
	}

	public boolean canStayOn() {
		return true;
	}

	public abstract Surface getSurface();

}
