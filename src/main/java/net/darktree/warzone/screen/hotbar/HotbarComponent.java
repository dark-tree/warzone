package net.darktree.warzone.screen.hotbar;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;

public abstract class HotbarComponent {

	protected String getName() {
		return null;
	}

	protected abstract void draw(boolean focused, World world, Symbol symbol);

}
