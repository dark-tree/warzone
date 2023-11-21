package net.darktree.warzone.screen.hotbar;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.WorldAccess;

public abstract class HotbarComponent {

	protected String getNameKey() {
		return null;
	}

	protected abstract void draw(boolean focused, WorldAccess world, Symbol symbol);

}
