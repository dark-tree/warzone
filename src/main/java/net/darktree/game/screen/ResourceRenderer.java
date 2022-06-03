package net.darktree.game.screen;

import net.darktree.game.country.Country;

@FunctionalInterface
public interface ResourceRenderer {

	void draw(Country country);

}
