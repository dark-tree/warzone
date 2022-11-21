package net.darktree.warzone.network;

public enum Side {
	HOST,
	CLIENT;

	public void expect(Side expected) {
		if (this != expected) throw new AssertionError("Invalid side!");
	}
}
