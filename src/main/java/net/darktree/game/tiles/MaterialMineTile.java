package net.darktree.game.tiles;

import net.darktree.core.event.ClickEvent;
import net.darktree.core.world.World;
import net.darktree.core.world.tile.Tile;
import net.darktree.core.world.tile.variant.TileVariant;
import net.darktree.core.world.tile.variant.property.BooleanProperty;
import net.darktree.game.country.Symbol;

public class MaterialMineTile extends Tile {

	public static BooleanProperty PROP1 = new BooleanProperty("test1");
	public static BooleanProperty PROP2 = new BooleanProperty("test2");

	public boolean canPathfindThrough(World world, int x, int y) {
		return false;
	}

	@Override
	public void onInteract(World world, int x, int y, ClickEvent event) {
		if (event.isPressed()) {
			world.setTileVariant(x, y, Tiles.MATERIAL.getDefaultVariant());
		}
	}

	@Override
	public void onPlayerTurnStart(World world, int x, int y, Symbol symbol) {
		if (symbol == world.getTileState(x, y).getOwner()) {
			world.getCountry(symbol).addMaterials(1);
		}
	}

	@Override
	public TileVariant getDefaultVariant() {
		return super.getDefaultVariant().with(PROP2, true);
	}

	protected TileVariant createDefaultVariant() {
		return TileVariant.createOf(this, PROP1, PROP2);
	}

}
