package net.darktree.warzone.client.window.input;

public class ClickEvent {

	public final int mods;
	public final ClickAction action;
	public final MouseButton button;

	public ClickEvent(int button, int action, int mods) {
		this.mods = mods;
		this.action = ClickAction.fromCode(action);
		this.button = MouseButton.fromCode(button);
	}

	public boolean isLeftClick() {
		return isPressed() && button == MouseButton.LEFT;
	}

	public boolean isRightClick() {
		return isPressed() && button == MouseButton.RIGHT;
	}

	public boolean isPressed() {
		return action == ClickAction.PRESS;
	}

}
