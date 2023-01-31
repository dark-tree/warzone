package net.darktree.warzone.client.window.input;

import org.lwjgl.glfw.GLFW;

public enum MouseButton {
	LEFT(GLFW.GLFW_MOUSE_BUTTON_1),
	MIDDLE(GLFW.GLFW_MOUSE_BUTTON_3),
	RIGHT(GLFW.GLFW_MOUSE_BUTTON_2);

	private final int code;

	MouseButton(int code) {
		this.code = code;
	}

	public static MouseButton fromCode(int button) {
		if (button == GLFW.GLFW_MOUSE_BUTTON_1) return LEFT;
		if (button == GLFW.GLFW_MOUSE_BUTTON_2) return RIGHT;
		if (button == GLFW.GLFW_MOUSE_BUTTON_3) return MIDDLE;

		return null;
	}

	public int getCode() {
		return code;
	}
}
