package net.darktree.warzone.world.entity.building;

import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.world.WorldAccess;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.action.DeconstructBuildingAction;
import net.darktree.warzone.world.entity.Entities;

public class MineBuilding extends Building {

	public MineBuilding(WorldSnapshot world, int x, int y) {
		super(world, x, y, Entities.MINE);
	}

	public int getIncome() {
		return 1;
	}

	public void onInteract(WorldAccess view, int x, int y, ClickEvent event) {
		if (view.getActiveSymbol() == world.getTileState(this.tx, this.ty).getOwner()) {
			view.getLedger().push(new DeconstructBuildingAction(getX(), getY()));
		}
	}

}
