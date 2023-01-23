package net.darktree.warzone.client.window;

import net.darktree.warzone.client.window.input.MouseButton;
import net.darktree.warzone.screen.ScreenStack;
import net.darktree.warzone.world.WorldView;
import org.lwjgl.glfw.GLFW;

public class Input {

	public static final float MAP_ZOOM_MIN = 0.07f;
	public static final float MAP_ZOOM_MAX = 1.00f;

	private final Window window;

	private boolean clicked;
	public float prevX;
	public float prevY;
	public float guiScale = 1;

	public Input(Window window) {
		this.window = window;
	}

	public void setGuiScale(float scale) {
		this.guiScale = scale;
	}

	void keyHandle(long handle, int key, int scancode, int action, int mods) {
		ScreenStack.asFocused(screen -> screen.onKey(key, action, mods));
	}

	void cursorHandle(long handle, double x, double y) {
		ScreenStack.asFocused(screen -> screen.onMove((float) x, (float) y));

		prevX = (float) x;
		prevY = (float) y;
	}

	void clickHandle(long handle, int button, int action, int mods) {
		ScreenStack.asFocused(screen -> screen.onClick(button, action, mods));

		// if button was RELEASED the click is complete
		clicked = (button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_RELEASE);
	}

	void scrollHandle(long handle, double x, double y) {
		ScreenStack.asFocused(screen -> screen.onScroll((float) y));
	}

	void frameHandle() {
		clicked = false;
	}

	public void updateScale(int w, int h) {
		ScreenStack.forEach(screen -> screen.onResize(w, h));
	}

	public float getMouseScreenX() {
		return (prevX / window.width() * 2 - 1);
	}

	public float getMouseScreenY() {
		return (prevY / window.height() * -2 + 1);
	}

	public int getMouseMapX(WorldView view) {
		return (int) Math.floor(getMouseScreenX() / view.scaleX - view.offsetX);
	}

	public int getMouseMapY(WorldView view) {
		return (int) Math.floor(getMouseScreenY() / view.scaleY - view.offsetY);
	}

	public boolean isKeyPressed(int key) {
		return GLFW.glfwGetKey(window.handle, key) == GLFW.GLFW_PRESS;
	}

	public boolean isButtonPressed(MouseButton button) {
		return GLFW.glfwGetMouseButton(window.handle, button.code) == GLFW.GLFW_PRESS;
	}

	public boolean hasClicked() {
		return clicked;
	}

}
