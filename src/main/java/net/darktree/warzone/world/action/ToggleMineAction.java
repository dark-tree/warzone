package net.darktree.warzone.world.action;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.building.Building;
import net.darktree.warzone.world.entity.building.MineBuilding;
import net.darktree.warzone.world.tile.tiles.Tiles;

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
