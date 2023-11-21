package net.darktree.warzone.world.terrain;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.WorldSnapshot;

public class Bonus {

	public final Symbol owner;
	public final int x;
	public final int y;

	public Bonus(int x, int y, Symbol owner) {
		this.owner = owner;
		this.x = x;
		this.y = y;
	}

	/**
	 * Transfers this bonus tile to the new owner
	 */
	public void transfer(WorldSnapshot world) {
		world.getTileState(x, y).setOwner(owner, true);
	}

}
