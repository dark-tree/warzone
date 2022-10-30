package net.darktree.warzone.world.entity.building;

import net.darktree.warzone.event.ClickEvent;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.action.ToggleMineAction;
import net.darktree.warzone.world.tile.tiles.Tiles;

public class MineBuilding extends Building {

	public MineBuilding(World world, int x, int y) {
		super(world, x, y, Tiles.MINE);
	}

	public int getIncome() {
		return 1;
	}

	public void onInteract(World world, int x, int y, ClickEvent event) {
		if (event.isPressed()) {
			world.getManager().apply(new ToggleMineAction(x, y));
		}
	}

	@Override
	public boolean isDeconstructable() {
		return true;
	}

	@Override
	public void deconstruct() {
		this.world.getManager().apply(new ToggleMineAction(tx, ty));
	}

}
