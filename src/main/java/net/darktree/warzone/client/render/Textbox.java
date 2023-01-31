package net.darktree.warzone.client.render;

import net.darktree.warzone.client.Colors;
import org.lwjgl.glfw.GLFW;

import java.util.function.Predicate;

public class Textbox {

	private String value = "";
	private boolean selected;
	private final Predicate<String> validator;

	public Textbox(Predicate<String> validator) {
		this.validator = validator;
	}

	private char glfwToAscii(int code) {
		return (char) (' ' + (code - GLFW.GLFW_KEY_SPACE));
	}

	public void pop() {
		if (value.length() > 0) {
			value = value.substring(0, value.length() - 1);
		}
	}

	public void onKey(int keycode) {
		if (selected) {
			if (keycode == GLFW.GLFW_KEY_BACKSPACE) {
				pop();
				return;
			}

			if (keycode >= GLFW.GLFW_KEY_SPACE && keycode <= GLFW.GLFW_KEY_Z) {
				String prev = value;
				value += glfwToAscii(keycode);

				if (!valid()) {
					value = prev;
				}
			}
		}
	}

	public void unselect() {
		selected = false;
	}

	public void select() {
		selected = true;
	}

	public void draw(int count, int size, int height) {
		if (ScreenRenderer.button(value, count, size, height, true, selected ? Colors.TEXTBOX_SELECTED : null)) {
			selected = true;
		}
	}

	public String getValue() {
		return value;
	}

	public boolean valid() {
		return validator.test(value);
	}
}
