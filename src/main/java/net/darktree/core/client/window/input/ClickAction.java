package net.darktree.core.client.window.input;

import org.lwjgl.glfw.GLFW;

public enum ClickAction {
	PRESS,
	RELEASE,
	OTHER;

	public static ClickAction fromCode(int action) {
		if (action == GLFW.GLFW_PRESS) return PRESS;
		if (action == GLFW.GLFW_RELEASE) return RELEASE;

		return OTHER;
	}
}
