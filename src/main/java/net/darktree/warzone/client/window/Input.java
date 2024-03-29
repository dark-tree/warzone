package net.darktree.warzone.client.window;

import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.client.window.input.KeyEvent;
import net.darktree.warzone.client.window.input.MouseButton;
import net.darktree.warzone.screen.ScreenStack;
import net.darktree.warzone.util.math.MathHelper;
import net.darktree.warzone.world.WorldView;
import org.lwjgl.glfw.GLFW;

public class Input {

	public static final float MAP_ZOOM_MIN = 0.07f;
	public static final float MAP_ZOOM_MAX = 1.00f;

	private final Window window;

	private boolean clicked;
	public float prevX;
	public float prevY;
	public float guiScale = 1f;

	public Input(Window window) {
		this.window = window;
	}

	public void setGuiScale(float scale) {
		this.guiScale = scale;
	}

	void keyHandle(long handle, int key, int scancode, int action, int mods) {
		KeyEvent event = new KeyEvent(key, scancode, action, mods);
		ScreenStack.asFocused(screen -> screen.onKey(event));
	}

	void cursorHandle(long handle, double x, double y) {
		ScreenStack.asFocused(screen -> screen.onMove((float) x, (float) y));

		prevX = (float) x;
		prevY = (float) y;
	}

	void clickHandle(long handle, int button, int action, int mods) {
		ClickEvent event = new ClickEvent(button, action, mods);
		ScreenStack.asFocused(screen -> screen.onClick(event));

		// if button was RELEASED the click is complete
		clicked = event.hasClicked();
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

	public float getMouseMapX(WorldView view) {
		return getMouseScreenX() / view.scaleX - view.offsetX;
	}

	public float getMouseMapY(WorldView view) {
		return getMouseScreenY() / view.scaleY - view.offsetY;
	}

	public int getMouseTileX(WorldView view) {
		return (int) Math.floor(getMouseMapX(view));
	}

	public int getMouseTileY(WorldView view) {
		return (int) Math.floor(getMouseMapY(view));
	}

	public float getMouseUiX() {
		return 2 * prevX - window.width();
	}

	public float getMouseUiY() {
		return window.height() - 2 * prevY;
	}

	public boolean isKeyPressed(int key) {
		return GLFW.glfwGetKey(window.handle, key) == GLFW.GLFW_PRESS;
	}

	public boolean isButtonPressed(MouseButton button) {
		return GLFW.glfwGetMouseButton(window.handle, button.getCode()) == GLFW.GLFW_PRESS;
	}

	public boolean hasClicked() {
		return clicked;
	}

	/**
	 * An ugly hack to translate glfw keys to ASCII
	 */
	public static char glfwToAscii(int code) {
		return (char) MathHelper.clamp(' ' + (code - GLFW.GLFW_KEY_SPACE), 32, 126);
	}

}
