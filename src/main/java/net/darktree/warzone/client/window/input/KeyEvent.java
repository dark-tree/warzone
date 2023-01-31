package net.darktree.warzone.client.window.input;

import net.darktree.warzone.client.window.Input;
import org.lwjgl.glfw.GLFW;

public class KeyEvent {

	public final int key;
	public final int scancode;
	public final int mods;
	public final KeyAction action;

	public KeyEvent(int key, int scancode, int action, int mods) {
		this.key = key;
		this.scancode = scancode;
		this.mods = mods;
		this.action = KeyAction.fromCode(action);
	}

	public boolean isTyped() {
		return action.isTyped();
	}

	public boolean isPrintable() {
		return key >= GLFW.GLFW_KEY_SPACE && key <= GLFW.GLFW_KEY_RIGHT_BRACKET;
	}

	public boolean isPressed(int key) {
		return action == KeyAction.PRESS && this.key == key;
	}

	public boolean isEscape() {
		return isPressed(GLFW.GLFW_KEY_ESCAPE);
	}

	public char getAscii() {
		return Input.glfwToAscii(key);
	}

}
