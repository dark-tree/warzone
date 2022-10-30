package net.darktree.warzone.screen;

import net.darktree.warzone.country.Country;

@FunctionalInterface
public interface ResourceRenderer {

	void draw(Country country);

}
