package net.darktree.core.world.action;

import net.darktree.core.world.World;
import net.darktree.core.world.entity.building.Building;
import net.darktree.core.world.entity.building.MineBuilding;
import net.darktree.core.world.tiles.Tiles;
import net.darktree.game.country.Symbol;

public class ToggleMineAction extends ToggleableAction {

	private final int x, y;

	public ToggleMineAction(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	boolean verify(World world, Symbol symbol) {
		return (world.getTileState(x, y).getTile() == Tiles.MATERIAL) && world.canControl(x, y, symbol);
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
		Building building = world.getBuilding(x, y);

		if (building == null) {
			world.addEntity(new MineBuilding(world, x, y));
		} else {
			if (building instanceof MineBuilding) {
				building.remove();
			}
		}
	}

	@Override
	public boolean supersedes(ToggleableAction peek) {
		return peek instanceof ToggleMineAction action && action.x == this.x && action.y == this.y;
	}

}
