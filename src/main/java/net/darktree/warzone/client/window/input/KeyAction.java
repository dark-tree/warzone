package net.darktree.warzone.client.window.input;

import org.lwjgl.glfw.GLFW;

public enum KeyAction {
	PRESS(true, GLFW.GLFW_PRESS),
	RELEASE(false, GLFW.GLFW_RELEASE),
	REPEAT(true, GLFW.GLFW_REPEAT);

	private final boolean typed;
	private final int code;

	KeyAction(boolean typed, int code) {
		this.typed = typed;
		this.code = code;
	}

	public static KeyAction fromCode(int action) {
		if (action == GLFW.GLFW_PRESS) return PRESS;
		if (action == GLFW.GLFW_RELEASE) return RELEASE;
		if (action == GLFW.GLFW_REPEAT) return REPEAT;

		return null;
	}

	public boolean isTyped() {
		return typed;
	}

	public int getCode() {
		return code;
	}
}
