package net.darktree.warzone.world.action.ledger;

public enum UndoBehaviour {
	BLOCKED,
	REPLAY;

	public boolean isRevertible() {
		return this == REPLAY;
	}
}
