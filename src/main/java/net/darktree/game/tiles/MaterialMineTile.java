package net.darktree.game.tiles;

import net.darktree.core.event.ClickEvent;
import net.darktree.core.world.World;
import net.darktree.core.world.action.ToggleMineAction;
import net.darktree.core.world.tile.Tile;
import net.darktree.game.country.Symbol;

public class MaterialMineTile extends Tile {

	public boolean canPathfindThrough(World world, int x, int y) {
		return false;
	}

	@Override
	public void onInteract(World world, int x, int y, ClickEvent event) {
		if (event.isPressed()) {
			world.getManager().apply(new ToggleMineAction(x, y));
		}
	}

	@Override
	public void onPlayerTurnStart(World world, int x, int y, Symbol symbol) {
		if (world.canControl(x, y, symbol)) {
			world.getCountry(symbol).addMaterials(1);
		}
	}

}
