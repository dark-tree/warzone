package net.darktree.warzone.client.render;

import net.darktree.warzone.client.gui.event.TextboxStateListener;
import net.darktree.warzone.client.window.input.KeyEvent;
import org.lwjgl.glfw.GLFW;

import java.util.function.Predicate;

public class Textbox {

	private String value = "";
	private final Predicate<String> validator;

	public Textbox(Predicate<String> validator) {
		this.validator = validator;
	}

	public void pop() {
		if (!value.isEmpty()) {
			value = value.substring(0, value.length() - 1);
		}
	}

	public void onKey(TextboxStateListener listener, KeyEvent event) {
		boolean prev = isValid();

		if (event.isTyped()) {
			if (event.key == GLFW.GLFW_KEY_BACKSPACE) {
				pop();

				listener.handle(prev, isValid(), value);
				return;
			}

			if (event.isPrintable()) {
				String old = value;
				value += event.getAscii();
				boolean next = isValid();

				if (!next) {
					value = old;
				}

				listener.handle(prev, next, value);
			}
		}
	}

	public String getValue() {
		return value;
	}

	public boolean isValid() {
		return validator.test(value);
	}

}
