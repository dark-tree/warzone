package net.darktree.warzone.world;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.event.TurnEvent;

public interface WorldListener {

	/**
	 * Called when a turn of 'symbol' player starts
	 */
	default void onPlayerTurnStart(Symbol symbol) {

	}

	/**
	 * Called when a turn of 'symbol' player end
	 */
	default void onPlayerTurnEnd(Symbol symbol) {

	}

	/**
	 * Called when a turn cycle has ended
	 */
	default void onTurnCycleEnd() {

	}

	/**
	 * Turn even dispatcher
	 */
	default void onPlayerTurnEvent(TurnEvent event, Symbol symbol) {
		if (event == TurnEvent.TURN_START) onPlayerTurnStart(symbol);
		if (event == TurnEvent.TURN_END) onPlayerTurnEnd(symbol);
		if (event == TurnEvent.TURN_CYCLE_END) onTurnCycleEnd();
	}

}