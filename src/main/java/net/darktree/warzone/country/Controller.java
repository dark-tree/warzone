package net.darktree.warzone.country;

import net.darktree.warzone.world.WorldSnapshot;

public abstract class Controller {

	/**
	 * Controls if this controller allows the local player to take action
	 */
	public abstract boolean isSelf();

	/**
	 * Called when a turn starts for this player
	 */
	public abstract void turnStart(Country country, WorldSnapshot world);

}
