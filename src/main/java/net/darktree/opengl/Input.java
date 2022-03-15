package net.darktree.opengl;

import org.lwjgl.glfw.GLFW;

public class Input {

	private final Window window;

	private float prevX;
	private float prevY;
	private float zoomMin;
	private float zoomMax;
	private float scale;

	// TODO make not public (maybe)
	public float offsetX;
	public float offsetY;
	public float zoom = 0.1f;
	public float scaleX;
	public float scaleY;

	public Input(Window window) {
		this.window = window;
	}

	public void setZoomRange(float min, float max) {
		this.zoomMin = min;
		this.zoomMax = max;
	}

	public void setScale(float scale) {
		this.scale = scale;
		resizeHandle();
	}

	void keyHandle(long handle, int key, int scancode, int action, int mods) {

	}

	void cursorHandle(long handle, double x, double y) {
		if (this.isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
			var ox = (prevX - x) / window.width() * -2/scaleX;
			var oy = (prevY - y) / window.height() * 2/scaleY;

			offsetX += ox;
			offsetY += oy;
		}

		prevX = (float) x;
		prevY = (float) y;
	}

	void resizeHandle() {
		scaleX = scale * window.height() / (float)window.width();
		scaleY = scale * 1;
	}

	// time_wasted_while_trying_to_fucking_make_this_work_again = 2h
	void scrollHandle(long handle, double x, double y) {
		zoom += (float) (zoom * y * 0.15f);

		if (zoom < zoomMin) zoom = zoomMin;
		if (zoom > zoomMax) zoom = zoomMax;
	}

	public boolean isKeyPressed(int key) {
		return GLFW.glfwGetKey(window.handle, key) == GLFW.GLFW_PRESS;
	}

	public boolean isButtonPressed(int button) {
		return GLFW.glfwGetMouseButton(window.handle, button) == GLFW.GLFW_PRESS;
	}

}
