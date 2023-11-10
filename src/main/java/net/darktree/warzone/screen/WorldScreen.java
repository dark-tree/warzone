package net.darktree.warzone.screen;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.render.Screen;
import net.darktree.warzone.client.render.WorldBuffers;
import net.darktree.warzone.client.window.Input;
import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.client.window.input.KeyEvent;
import net.darktree.warzone.client.window.input.MouseButton;
import net.darktree.warzone.screen.interactor.Interactor;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.WorldView;

public abstract class WorldScreen extends Screen {

	protected final World world;
	protected final WorldBuffers buffers;
	protected boolean isMapFocused = true;
	protected Interactor interactor = null;

	protected WorldScreen(World world) {
		this.world = world;
		this.buffers = new WorldBuffers(world.getView());
	}

	public static void setInteractor(Interactor interactor) {
		ScreenStack.asInstance(WorldScreen.class, screen -> screen.interactor = interactor);
	}

	protected abstract void onWorldKey(KeyEvent event);
	protected abstract void onWorldClick(ClickEvent event, int x, int y);
	protected abstract void onWorldEscape();

	@Override
	public void draw(boolean focused) {
		this.world.getView().draw(buffers);
		isMapFocused = focused;

		if (interactor != null) {
			interactor.draw(buffers.getEntity(), buffers.getOverlay());

			if (interactor.isClosed()) {
				interactor.close();
				interactor = null;
			}
		}
	}

	@Override
	public void onKey(KeyEvent event) {
		super.onKey(event);

		if (interactor != null) {
			interactor.onKey(event);
			return;
		}

		onWorldKey(event);
	}

	@Override
	public void onClick(ClickEvent event) {
		if (!isMapFocused || event.button == MouseButton.MIDDLE) {
			return;
		}

		Input input = Main.window.input();
		int x = input.getMouseTileX(world.getView());
		int y = input.getMouseTileY(world.getView());

		if (interactor != null) {
			interactor.onClick(event, x, y);
			return;
		}

		// every action can be performed with right click, is this intended?

		onWorldClick(event, x, y);
	}

	@Override
	public void onEscape() {
		if (interactor != null) {
			return;
		}

		onWorldEscape();
	}


	@Override
	public void onResize(int w, int h) {
		WorldView view = world.getView();

		view.setZoom(view.zoom);
	}

	@Override
	public void onScroll(float value) {
		WorldView view = world.getView();

		float zoom = view.zoom * (1 + value * 0.15f);

		if (zoom < Input.MAP_ZOOM_MIN) zoom = Input.MAP_ZOOM_MIN;
		if (zoom > Input.MAP_ZOOM_MAX) zoom = Input.MAP_ZOOM_MAX;

		view.setZoom(zoom);
	}

	public void onMove(float x, float y) {
		Input input = Main.window.input();

		if (input.isButtonPressed(MouseButton.MIDDLE)) {
			float ox = (input.prevX - x) / Main.window.width() * -2;
			float oy = (input.prevY - y) / Main.window.height() * 2;

			world.getView().drag(ox, oy);
		}
	}

	@Override
	public void onRemoved() {
		super.onRemoved();
		this.buffers.close();
	}

}
