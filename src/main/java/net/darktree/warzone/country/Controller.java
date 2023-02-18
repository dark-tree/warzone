package net.darktree.warzone.country;

import net.darktree.warzone.world.World;

public abstract class Controller {

	public abstract boolean isSelf();

	public abstract void turnStart(Country country, World world);

}
