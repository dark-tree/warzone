package net.darktree.game.buildings;

import net.darktree.core.event.ClickEvent;
import net.darktree.core.util.BuildingType;
import net.darktree.core.world.World;
import net.darktree.game.screen.ProduceScreen;
import net.darktree.game.screen.ScreenStack;

public class FactoryBuilding extends Building {

	public FactoryBuilding(World world, int x, int y, BuildingType type) {
		super(world, x, y, type);
	}

	@Override
	public void onInteract(World world, int x, int y, ClickEvent event) {
		if (event.isPressed()) {
			ScreenStack.open(new ProduceScreen());
		}
	}

}
