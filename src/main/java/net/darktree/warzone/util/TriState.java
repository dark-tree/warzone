package net.darktree.warzone.util;

public enum TriState {
	TRUE(true),
	DEFAULT(false),
	FALSE(false);

	private final boolean value;

	TriState(boolean value) {
		this.value = value;
	}

	public boolean orDefaultTo(boolean bool) {
		return (this == DEFAULT) ? bool : value;
	}

	public static TriState of(boolean bool) {
		return bool ? TRUE : FALSE;
	}
}
