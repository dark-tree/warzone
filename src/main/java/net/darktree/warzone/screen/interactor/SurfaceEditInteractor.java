package net.darktree.warzone.screen.interactor;

import net.darktree.warzone.client.render.image.Sprite;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.tile.SpriteBridge;
import net.darktree.warzone.world.tile.Tile;
import net.darktree.warzone.world.tile.tiles.Tiles;
import net.darktree.warzone.world.tile.variant.TileVariant;

public class SurfaceEditInteractor extends BrushInteractor<TileVariant> {

	public SurfaceEditInteractor(Tile tile, World world, int radius) {
		super(tile.getDefaultVariant(), world, radius);
	}

	@Override
	protected void place(TileVariant material, int x, int y, boolean erase) {
		world.setTileVariant(x, y, erase ? Tiles.EMPTY.getDefaultVariant() : material);
	}

	@Override
	protected Sprite sprite(TileVariant material) {
		return SpriteBridge.getSprite(material.getTile());
	}

}
