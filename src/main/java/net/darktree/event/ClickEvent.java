package net.darktree.event;

import net.darktree.lt2d.input.ClickAction;
import net.darktree.lt2d.input.MouseButton;

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
