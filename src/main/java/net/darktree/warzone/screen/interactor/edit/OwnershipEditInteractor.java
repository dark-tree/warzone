package net.darktree.warzone.screen.interactor.edit;

import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.image.Sprite;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.WorldAccess;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.terrain.BonusFinder;

public class OwnershipEditInteractor extends BrushInteractor<Symbol> {

	public OwnershipEditInteractor(Symbol target, WorldAccess world, int radius) {
		super(target, world, radius);
	}

	@Override
	protected void place(Symbol material, int x, int y, boolean erase) {
		place(x, y, erase ? Symbol.NONE : material);
	}

	@Override
	protected Sprite sprite(Symbol material) {
		return Sprites.BLANK;
	}

	private void place(int x, int y, Symbol symbol) {
		WorldSnapshot snapshot = world.getTrackingWorld();

		snapshot.getTileState(x, y).setOwner(symbol, true);
		new BonusFinder(snapshot, symbol).grant(); // TODO
	}

}
