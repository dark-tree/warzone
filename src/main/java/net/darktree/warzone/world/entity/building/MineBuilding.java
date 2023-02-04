package net.darktree.warzone.world.entity.building;

import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.action.DeconstructBuildingAction;
import net.darktree.warzone.world.entity.Entities;

public class MineBuilding extends Building {

	public MineBuilding(World world, int x, int y) {
		super(world, x, y, Entities.MINE);
	}

	public int getIncome() {
		return 1;
	}

	public void onInteract(World world, int x, int y, ClickEvent event) {
		if (event.isPressed() && world.getActiveSymbol() == world.getTileState(this.tx, this.ty).getOwner()) {
			world.getManager().apply(new DeconstructBuildingAction(world, getX(), getY()));
		}
	}

}
