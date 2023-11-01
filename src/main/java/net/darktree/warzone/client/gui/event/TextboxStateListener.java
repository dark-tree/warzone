package net.darktree.warzone.client.gui.event;

@FunctionalInterface
public interface TextboxStateListener {
	void handle(boolean prev, boolean next, String value);
}
