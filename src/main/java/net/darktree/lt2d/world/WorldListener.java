package net.darktree.lt2d.world;

import net.darktree.event.TurnEvent;
import net.darktree.game.country.Symbol;

public interface WorldListener {

	/**
	 * Called when a turn of 'symbol' player starts
	 */
	default void onPlayerTurnStart(World world, int x, int y, Symbol symbol) {

	}

	/**
	 * Called when a turn of 'symbol' player end
	 */
	default void onPlayerTurnEnd(World world, int x, int y, Symbol symbol) {

	}

	/**
	 * Called when a turn cycle has ended
	 */
	default void onTurnCycleEnd(World world, int x, int y) {

	}

	/**
	 * Turn even dispatcher
	 */
	default void onPlayerTurnEvent(World world, int x, int y, TurnEvent event, Symbol symbol) {
		if (event == TurnEvent.TURN_START) onPlayerTurnStart(world, x, y, symbol);
		if (event == TurnEvent.TURN_END) onPlayerTurnEnd(world, x, y, symbol);
		if (event == TurnEvent.TURN_CYCLE_END) onTurnCycleEnd(world, x, y);
	}

}
