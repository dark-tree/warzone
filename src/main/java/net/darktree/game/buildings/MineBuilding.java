package net.darktree.game.buildings;

import net.darktree.core.event.ClickEvent;
import net.darktree.core.world.World;
import net.darktree.core.world.action.ToggleMineAction;
import net.darktree.core.world.tile.MaterialProvider;
import net.darktree.game.tiles.Tiles;

public class MineBuilding extends Building implements MaterialProvider {

	public MineBuilding(World world, int x, int y) {
		super(world, x, y, Tiles.MINE);
	}

	@Override
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
