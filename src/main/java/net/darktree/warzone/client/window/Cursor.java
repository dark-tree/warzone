package net.darktree.warzone.client.window;

import org.lwjgl.glfw.GLFW;

public class Cursor implements AutoCloseable {

	private final long cursor;

	Cursor(int shape) {
		this.cursor = GLFW.glfwCreateStandardCursor(shape);
	}

	public void set(Window window) {
		GLFW.glfwSetCursor(window.handle, cursor);
	}

	@Override
	public void close() {
		GLFW.glfwDestroyCursor(cursor);
	}

}
