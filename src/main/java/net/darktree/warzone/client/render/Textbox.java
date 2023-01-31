package net.darktree.warzone.client.render;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.window.input.KeyEvent;
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

	public void onKey(KeyEvent event) {
		if (selected && event.isTyped()) {
			if (event.key == GLFW.GLFW_KEY_BACKSPACE) {
				pop();
				return;
			}

			if (event.isPrintable()) {
				String prev = value;
				value += event.getAscii();

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
