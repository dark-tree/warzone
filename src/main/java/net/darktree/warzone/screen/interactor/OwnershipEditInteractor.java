package net.darktree.warzone.screen.interactor;

import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.image.Sprite;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;

public class OwnershipEditInteractor extends BrushInteractor<Symbol> {

	public OwnershipEditInteractor(Symbol target, World world, int radius) {
		super(target, world, radius);
	}

	@Override
	protected void place(Symbol material, int x, int y, boolean erase) {
		place(x, y, erase ? Symbol.NONE : material);
	}

	@Override
	protected Sprite sprite(Symbol material) {
		return Sprites.NONE;
	}

	private void place(int x, int y, Symbol symbol) {
		world.setTileOwner(x, y, symbol);
		world.grantBonusTiles(symbol);
	}

}
