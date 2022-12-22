package net.darktree.warzone.screen;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.render.Alignment;
import net.darktree.warzone.client.render.Screen;
import net.darktree.warzone.client.render.ScreenRenderer;

import java.util.Stack;
import java.util.function.Consumer;

public class ScreenStack {

	private static final Stack<Screen> stack = new Stack<>();

	public static void draw() {
		int last = stack.size() - 1;

		for (int i = 0; i < stack.size(); i ++) {
			boolean focused = (i == last);

			ScreenRenderer.setFocus(focused);
			ScreenRenderer.setOffset(0, 0);
			ScreenRenderer.setAlignment(Alignment.LEFT);
			ScreenRenderer.setColor(Colors.NONE);
			stack.get(i).draw(focused);
			ScreenRenderer.flush();
		}

		stack.removeIf(Screen::isClosed);
	}

	public static void open(Screen screen) {
		stack.push(screen);
	}

	public static void closeAll() {
		stack.clear();
	}

	public static void asFocused(Consumer<Screen> consumer) {
		if (!stack.isEmpty()) {
			consumer.accept(stack.peek());
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends Screen> void asInstance(Class<T> clazz, Consumer<T> consumer) {
		for (Screen screen : stack) {
			if (clazz.isInstance(screen)) {
				consumer.accept((T) screen);
				break;
			}
		}
	}

}
