package net.darktree.warzone.client.render;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.image.Sprite;
import net.darktree.warzone.client.text.Text;
import org.lwjgl.glfw.GLFW;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class Screen {

	private boolean closed;

	public static final Text TEXT_YES = Text.translated("gui.yes");
	public static final Text TEXT_NO = Text.translated("gui.no");
	public static final Text TEXT_PAGE = Text.translated("gui.page");

	public Screen() {
		closed = false;
	}

	protected void text(int ox, int oy, String text, Alignment alignment) {
		ScreenRenderer.push();
		ScreenRenderer.setOffset(ox, oy);
		ScreenRenderer.setAlignment(alignment);
		ScreenRenderer.literalText(30, text);
		ScreenRenderer.pop();
	}

	protected void drawBackground() {
		ScreenRenderer.centerAt(-1, -1);
		ScreenRenderer.setSprite(Sprites.NONE);
		ScreenRenderer.setColor(Colors.SCREEN_SEPARATOR);
		ScreenRenderer.box(Main.window.width() * 2, Main.window.height() * 2);
	}

	protected void drawTitledScreen(CharSequence title, CharSequence subtitle, Sprite sprite, int width, int height) {
		drawBackground();
		ScreenRenderer.centerAt(0, 0);
		ScreenRenderer.setColor(Colors.NONE);
		ScreenRenderer.setSprite(sprite);
		ScreenRenderer.centeredBox(width, height);

		final int half = height / 2;
		final int y1 = half - 90;
		final int y2 = y1 - 40;

		text(0, y1, title.toString(), Alignment.CENTER);
		text(0, y2, subtitle.toString(), Alignment.CENTER);
	}

	public abstract void draw(boolean focused);

	@OverridingMethodsMustInvokeSuper
	public void onKey(int key, int action, int mods) {
		if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
			onEscape();
		}
	}

	public void onEscape() {
		this.close();
	}

	public void onClick(int button, int action, int mods) {

	}

	public void onScroll(float value) {

	}

	public void onMove(float x, float y) {

	}

	public void onResize(int w, int h) {

	}

	public void close() {
		this.closed = true;
		Sounds.PEN_CLOSE.play();
	}

	public boolean isClosed() {
		return this.closed;
	}

}
