package net.darktree.warzone.country.controller;

import net.darktree.warzone.country.Controller;

public class LocalController extends Controller {

	@Override
	public boolean isSelf() {
		return true;
	}

}
