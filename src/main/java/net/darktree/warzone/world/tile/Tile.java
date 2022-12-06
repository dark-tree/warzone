package net.darktree.warzone.world.tile;

import net.darktree.warzone.Registries;
import net.darktree.warzone.client.render.vertex.Renderer;
import net.darktree.warzone.client.render.vertex.VertexBuffer;
import net.darktree.warzone.util.ElementType;
import net.darktree.warzone.util.Registry;
import net.darktree.warzone.world.tile.variant.TileVariant;

public abstract class Tile extends ElementType<Tile> implements WorldTile {

	public final TileVariant variant;

	public Tile() {
		this.variant = createDefaultVariant();
	}

	protected TileVariant createDefaultVariant() {
		return TileVariant.createOf(this);
	}

	@Override
	public Registry<Tile> getRegistry() {
		return Registries.TILES;
	}

	@Override
	public void onRegister(String id) {
		SpriteBridge.register(this, id);
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
