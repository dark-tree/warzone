package net.darktree.warzone.client.window.input;

import org.lwjgl.glfw.GLFW;

public enum ClickAction {
	PRESS(GLFW.GLFW_PRESS),
	RELEASE(GLFW.GLFW_RELEASE);

	private final int code;

	ClickAction(int code) {
		this.code = code;
	}

	public static ClickAction fromCode(int action) {
		if (action == GLFW.GLFW_PRESS) return PRESS;
		if (action == GLFW.GLFW_RELEASE) return RELEASE;

		return null;
	}

	public int getCode() {
		return code;
	}
}
