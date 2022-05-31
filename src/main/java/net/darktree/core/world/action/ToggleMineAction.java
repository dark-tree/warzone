package net.darktree.core.world.action;

import net.darktree.core.world.World;
import net.darktree.core.world.tile.TileState;
import net.darktree.game.country.Symbol;
import net.darktree.game.tiles.Tiles;

public class ToggleMineAction extends Action {

	private final int x, y;

	public ToggleMineAction(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	boolean verify(World world, Symbol symbol) {
		TileState state = world.getTileState(x, y);
		return (state.getTile() == Tiles.MATERIAL_MINE || state.getTile() == Tiles.MATERIAL) && world.canControl(x, y, symbol);
	}

	@Override
	void redo(World world, Symbol symbol) {
		toggle(world);
	}

	@Override
	void undo(World world, Symbol symbol) {
		toggle(world);
	}

	private void toggle(World world) {
		TileState state = world.getTileState(x, y);

		if (state.getTile() == Tiles.MATERIAL) {
			world.setTileVariant(x, y, Tiles.MATERIAL_MINE.getDefaultVariant());
		} else if (state.getTile() == Tiles.MATERIAL_MINE) {
			world.setTileVariant(x, y, Tiles.MATERIAL.getDefaultVariant());
		}
	}

}
