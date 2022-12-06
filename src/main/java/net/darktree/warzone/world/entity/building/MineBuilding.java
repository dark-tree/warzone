package net.darktree.warzone.world.entity.building;

import net.darktree.warzone.event.ClickEvent;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.action.DeconstructBuildingAction;
import net.darktree.warzone.world.tile.tiles.Tiles;

public class MineBuilding extends Building {

	public MineBuilding(World world, int x, int y) {
		super(world, x, y, Tiles.MINE);
	}

	public int getIncome() {
		return 1;
	}

	public void onInteract(World world, int x, int y, ClickEvent event) {
		if (event.isPressed() && world.getCurrentSymbol() == world.getTileState(this.tx, this.ty).getOwner()) {
			world.getManager().apply(new DeconstructBuildingAction(world, getX(), getY()));
		}
	}

	@Override
	public boolean isDeconstructable() {
		return true;
	}

}
