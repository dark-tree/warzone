package net.darktree.warzone.event;

import net.darktree.warzone.client.window.input.ClickAction;
import net.darktree.warzone.client.window.input.MouseButton;

public class ClickEvent {
	public final MouseButton button;
	public final ClickAction action;

	public ClickEvent(int button, int action) {
		this.button = MouseButton.fromCode(button);
		this.action = ClickAction.fromCode(action);
	}

	public boolean isPressed() {
		return action == ClickAction.PRESS;
	}
}
