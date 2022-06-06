package net.darktree.game.screen.hotbar;

import net.darktree.core.world.World;
import net.darktree.game.country.Symbol;

public abstract class HotbarComponent {

	protected String getName() {
		return null;
	}

	protected abstract void draw(boolean focused, World world, Symbol symbol);

}
