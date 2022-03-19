package net.darktree.opengl;

import net.darktree.Main;
import net.darktree.util.Logger;
import org.lwjgl.glfw.GLFW;

public class Input {

	private final Window window;

	private float prevX;
	private float prevY;
	private float zoomMin;
	private float zoomMax;

	// TODO make not public (maybe)
	public float offsetX;
	public float offsetY;
	public float zoom = 0.1f;
	public float scaleX;
	public float scaleY;
	public float guiScale = 1;

	public Input(Window window) {
		this.window = window;
	}

	public void setZoomRange(float min, float max) {
		this.zoomMin = min;
		this.zoomMax = max;
	}

	public void setGuiScale(float scale) {
		this.guiScale = scale;
	}

	void keyHandle(long handle, int key, int scancode, int action, int mods) {

	}

	void cursorHandle(long handle, double x, double y) {
		if (this.isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_3)) {
			var ox = (prevX - x) / window.width() * -2 / scaleX;
			var oy = (prevY - y) / window.height() * 2 / scaleY;

			offsetX += ox;
			offsetY += oy;
		}

		prevX = (float) x;
		prevY = (float) y;
	}

	void clickHandle(long handle, int button, int action, int mods) {
		float x = ((prevX / window.width() * 2 / scaleX) - offsetX) * scaleX * Main.world.width/2;
		float y = ((prevY / window.height() * 2 / scaleY) + offsetY) * scaleY * Main.world.height;

		try {
			if(button == GLFW.GLFW_MOUSE_BUTTON_1 || button == GLFW.GLFW_MOUSE_BUTTON_2) {
				Main.world.getTile((int) x, Main.world.height - (int) y).interact(action);
			}
		}catch (Exception ignored) {

		}

		Logger.info(x, " ", y);
	}

	void resizeHandle() {
		scaleX = window.height() / (float) window.width();
		scaleY = 1;
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
