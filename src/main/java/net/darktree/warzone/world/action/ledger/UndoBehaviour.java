package net.darktree.warzone.world.action.ledger;

public enum UndoBehaviour {
	BLOCKED,
	JUST_DROP,
	REPLAY_ACTIONS;

	public boolean isRevertible() {
		return this == JUST_DROP || this == REPLAY_ACTIONS;
	}
}
