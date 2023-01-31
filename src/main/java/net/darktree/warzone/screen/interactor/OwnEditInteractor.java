package net.darktree.warzone.screen.interactor;

import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;

public class OwnEditInteractor extends Interactor {

	private final Symbol target;
	private final World world;

	public OwnEditInteractor(Symbol target, World world) {
		this.target = target;
		this.world = world;
	}

	@Override
	public void onClick(ClickEvent event, int x, int y) {
		if (world.isPositionValid(x, y)) {
			world.setTileOwner(x, y, target);
			world.grantBonusTiles(target);
		}
	}
}
