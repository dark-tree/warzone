package net.darktree.opengl;

import org.lwjgl.glfw.GLFW;

public class Input {

	private final Window window;

	private double prevX;
	private double prevY;
	public float offsetX; // TODO make not public
	public float offsetY;
	public float zoom = 0.1f;

	public Input(Window window) {
		this.window = window;
	}

	void keyHandle(long handle, int key, int scancode, int action, int mods) {

	}

	void cursorHandle(long handle, double x, double y) {
		if (GLFW.glfwGetMouseButton(handle, GLFW.GLFW_MOUSE_BUTTON_1) == GLFW.GLFW_PRESS) {
			offsetX -= (prevX - x) / window.width() * 2;
			offsetY += (prevY - y) / window.height() * 2;
		}

		prevX = x;
		prevY = y;
	}

	void scrollHandle(long handle, double x, double y) {
		final float min = 0.07f;
		final float max = 1f;

		float border = border(zoom, min, max);

		float newZoom = zoom + (float) (y * border * 0.15f);

		if (border(newZoom, min, max) < border) {
			zoom = newZoom;
		}else{
			zoom += (float) (y * border * 0.3f);
		}
	}

	private float border(float value, float min, float max) {
		return 1 - Math.abs(((value - min) * 2 - (max - min)) / (max - min));
	}

	private float absoluteMinimum(float value, float min) {
		return (value < 0) ? Math.max(value, -min) : Math.min(value, min);
	}

}
