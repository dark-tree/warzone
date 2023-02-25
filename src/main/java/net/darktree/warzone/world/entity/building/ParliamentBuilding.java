package net.darktree.warzone.world.entity.building;

import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.screen.ParliamentScreen;
import net.darktree.warzone.screen.ScreenStack;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.Entities;

public class ParliamentBuilding extends Building {

	public ParliamentBuilding(World world, int x, int y) {
		super(world, x, y, Entities.PARLIAMENT);
	}

	@Override
	public void onInteract(World world, int x, int y, ClickEvent event) {
		getOwner().ifPresent(owner -> ScreenStack.open(new ParliamentScreen(world, owner.symbol)));
	}

}
