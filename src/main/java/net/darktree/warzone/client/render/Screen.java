package net.darktree.warzone.client.render;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.gui.GridContext;
import net.darktree.warzone.client.gui.ModelBuilder;
import net.darktree.warzone.client.gui.component.UiComposed;
import net.darktree.warzone.client.gui.prefab.GridContextFactory;
import net.darktree.warzone.client.gui.prefab.GridPrefabs;
import net.darktree.warzone.client.render.color.Color;
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

	/**
	 * Predefined {@link GridContextFactory} factories are
	 * available in {@link net.darktree.warzone.client.gui.prefab.GridPrefabs}, using
	 * {@code NONE} makes the screen not use Grid UI
	 */
	public Screen() {
		closed = false;
		context = getGridFactory().construct();
	}

	/**
	 * If the screen is to use the Grid UI, this method should be
	 * overriden and return a {@link GridContextFactory}. Predefined factories are
	 * available in {@link GridPrefabs}. By default, the {@code NONE} factory is used.
	 */
	protected GridContextFactory getGridFactory() {
		return GridPrefabs.NONE;
	}

	/**
	 * Marks the Grid UI model for rebuilding, this should be used
	 * if some aspect of the UI needs updating, and will cause the
	 * {@link Screen#buildModel} to be called again.
	 */
	protected void rebuildModel() {
		shouldRebuild = true;
	}

	@Deprecated
	protected void text(int ox, int oy, CharSequence text, Alignment alignment) {
		ScreenRenderer.push();
		ScreenRenderer.setOffset(ox, oy);
		ScreenRenderer.setAlignment(alignment);
		ScreenRenderer.text(30, text);
		ScreenRenderer.pop();
	}

	/**
	 * Used for creating the Grid UI model using {@link ModelBuilder},
	 * if the model needs to update to some change in the UI,
	 * call {@link Screen#rebuildModel()}
	 * <p>
	 * Grid UI operates on a grid of given dimensions, all UI
	 * components occupy some rectangle on that grid, specified by the
	 * grid coordinates counted from the bottom left corner of the grid (starting
	 * at [0, 0])
	 * <pre>{@code
	 *     // adds a button in the bottom left corner
	 *     // of width 6, height 2, and label "Press Me"
	 *     builder.add(0, 0, UiButton.of("Press Me").box(6, 2).react(() -> {
	 *         Logger.info("Pressed!");
	 *     }));
	 * }</pre>
	 * </p>
	 * <p>
	 * Grid UI components can be chained and overlayed on top of each other,
	 * this is the case so that the UI can be easily extended with limited
	 * conflicts, as well as simplifying the UI code and decreasing the number of
	 * direct grid references that can sometimes be hard to visualize when looking at
	 * a piece of code.
	 * <pre>{@code
	 *     // Adds a button "1" and then chains the buttons "2" and "3"
	 *     builder.add(0, 0, UiButton.of("1").box(6, 2).disable());
	 *     builder.then(Chain.AFTER, UiButton.of("2").box(6, 2).disable());
	 *
	 *     // this button will appear bellow the previous one
	 *     builder.then(Chain.BELOW, UiButton.of("3").box(6, 2).disable());
	 * }</pre>
	 * </p>
	 * <p>
	 * If spacing is necessary between two chained grid components, the
	 * {@link net.darktree.warzone.client.gui.component.UiNull} can be used, it
	 * behaves like a simple box of given size but is ignored during rendering.
	 * <pre>{@code
	 *     // Adds two spaced chained buttons
	 *     builder.add(0, 0, UiButton.of("1").box(6, 2).disable());
	 *     builder.then(Chain.AFTER, UiNull.of(1, 1));
	 *     builder.then(Chain.AFTER, UiButton.of("2").box(6, 2).disable());
	 * }</pre>
	 * </p>
	 */
	protected void buildModel(ModelBuilder builder) {

	}

	/**
	 * Used to render the Grid UI model of this screen,
	 * call this method near the beginning of {@link Screen#draw}
	 */
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

	/**
	 * Draws a solid rectangle of the given color that fills the whole window,
	 * usually used in {@link Screen#draw} with the {@link Colors#SCREEN_SEPARATOR} color.
	 */
	protected void drawBackground(Color color) {
		ScreenRenderer.setColorMode(ColorMode.MIXED);
		ScreenRenderer.centerAt(-1, -1);
		ScreenRenderer.setSprite(Sprites.NONE);
		ScreenRenderer.setColor(color);
		ScreenRenderer.box(Main.window.width() * 2, Main.window.height() * 2);
	}

	/**
	 * Called in a back-to-front order on all open screens every frame,
	 * override to render this screen.
	 */
	public abstract void draw(boolean focused);

	@OverridingMethodsMustInvokeSuper
	public void onKey(KeyEvent event) {
		if (event.isEscape()) onEscape();
		if (context != null) context.onEvent(event);
	}

	@OverridingMethodsMustInvokeSuper
	public void onClick(ClickEvent event) {
		if (context != null) context.onEvent(event);
	}

	/**
	 * Override to change the ESC keypress behaviour,
	 * by default closes the current window.
	 */
	public void onEscape() {
		this.close();
	}

	public void onScroll(float value) {

	}

	public void onMove(float x, float y) {

	}

	public void onResize(int w, int h) {

	}

	/**
	 * Called on a screen object after it is already removed from the screen stack
	 * this is the last method that will ever be called on this screen object
	 */
	@OverridingMethodsMustInvokeSuper
	public void onRemoved() {
		if (context != null) {
			context.getState().invalidate();
		}
	}

	/**
	 * Closes this screen and plays the "screen close" sound effect
	 */
	public final void close() {
		this.closed = true;
		Sounds.PEN_CLOSE.play();
	}

	/**
	 * Closes this screen and opens the one given instead
	 */
	public final void emplace(Screen screen) {
		this.close();
		ScreenStack.open(screen);
	}

	/**
	 * Checks if this screen is closed and therefore should be removed from the screen stack
	 */
	public boolean isClosed() {
		return this.closed;
	}

}
