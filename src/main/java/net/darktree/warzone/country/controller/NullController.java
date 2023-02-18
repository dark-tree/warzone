package net.darktree.warzone.country.controller;

import net.darktree.warzone.country.Controller;
import net.darktree.warzone.country.Country;
import net.darktree.warzone.world.World;

public class NullController extends Controller {

	@Override
	public boolean isSelf() {
		return false;
	}

	@Override
	public void turnStart(Country country, World world) {

	}

}
