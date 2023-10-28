package net.darktree.warzone.client.render;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.gui.GridContext;
import net.darktree.warzone.client.gui.ModelBuilder;
import net.darktree.warzone.client.gui.component.UiComposed;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.image.Sprite;
import net.darktree.warzone.client.text.Text;
import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.client.window.input.KeyEvent;
import net.darktree.warzone.screen.ScreenStack;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class Screen {

	private boolean closed;

	public static final Text TEXT_OKAY = Text.translated("gui.okay");
	public static final Text TEXT_OK = Text.translated("gui.ok");
	public static final Text TEXT_YES = Text.translated("gui.yes");
	public static final Text TEXT_NO = Text.translated("gui.no");
	public static final Text TEXT_PAGE = Text.translated("gui.page");

	private final GridContext context;
	private boolean shouldRebuild = true;

	public Screen() {
		closed = false;
		context = createGridContext();
	}

	protected GridContext createGridContext() {
		return null;
	}

	protected void rebuildModel() {
		shouldRebuild = true;
	}

	protected void text(int ox, int oy, CharSequence text, Alignment alignment) {
		ScreenRenderer.push();
		ScreenRenderer.setOffset(ox, oy);
		ScreenRenderer.setAlignment(alignment);
		ScreenRenderer.text(30, text);
		ScreenRenderer.pop();
	}

	protected void buildModel(ModelBuilder builder) {

	}

	protected void drawModel() {
		if (context != null) {
			if (shouldRebuild) {
				shouldRebuild = false;

				ModelBuilder builder = UiComposed.of();
				buildModel(builder);
				context.setModel(builder);
			}

			context.draw(ScreenRenderer.getDrawContext());
		}
	}

	protected void drawBackground(Color color) {
		ScreenRenderer.centerAt(-1, -1);
		ScreenRenderer.setSprite(Sprites.NONE);
		ScreenRenderer.setColor(color);
		ScreenRenderer.box(Main.window.width() * 2, Main.window.height() * 2);
	}

	protected void drawTitledScreen(CharSequence title, CharSequence subtitle, Sprite sprite, int width, int height) {
		drawBackground(Colors.SCREEN_SEPARATOR);
		ScreenRenderer.centerAt(0, 0);
		ScreenRenderer.setColor(Colors.NONE);
		ScreenRenderer.setSprite(sprite);
		ScreenRenderer.centeredBox(width, height);

		final int half = height / 2;
		final int y1 = half - 90;
		final int y2 = y1 - 40;

		ScreenRenderer.setColor(Colors.TEXT);
		text(0, y1, title, Alignment.CENTER);
		text(0, y2, subtitle, Alignment.CENTER);
	}

	public abstract void draw(boolean focused);

	@OverridingMethodsMustInvokeSuper
	public void onKey(KeyEvent event) {
		if (event.isEscape()) onEscape();
		if (context != null) context.react(event);
	}

	@OverridingMethodsMustInvokeSuper
	public void onClick(ClickEvent event) {
		if (context != null) context.react(event);
	}

	public void onEscape() {
		this.close();
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

	public void emplace(Screen screen) {
		this.close();
		ScreenStack.open(screen);
	}

	public boolean isClosed() {
		return this.closed;
	}

}
