package net.darktree.warzone.screen;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.Screen;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.client.render.WorldBuffers;
import net.darktree.warzone.client.window.Input;
import net.darktree.warzone.client.window.input.MouseButton;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.event.ClickEvent;
import net.darktree.warzone.network.UserGroup;
import net.darktree.warzone.network.packet.EndTurnPacket;
import net.darktree.warzone.screen.hotbar.Hotbar;
import net.darktree.warzone.screen.interactor.Interactor;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.WorldHolder;
import net.darktree.warzone.world.WorldView;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.overlay.MapOverlay;
import org.lwjgl.glfw.GLFW;

public class PlayScreen extends Screen {

	private Interactor interactor = null;

	public static void setInteractor(Interactor interactor) {
		ScreenStack.asInstance(PlayScreen.class, screen -> screen.interactor = interactor);
	}

	private boolean isMapFocused = true;
	private final World world;

	public PlayScreen(World world) {
		this.world = world;
	}

	@Override
	public void draw(boolean focused) {

		isMapFocused = focused;

		WorldHolder.draw();

		if (interactor != null) {
			WorldBuffers buffers = WorldHolder.buffers;
			interactor.draw(buffers.getEntity(), buffers.getOverlay());

			if (interactor.isClosed()) {
				interactor.close();
				interactor = null;
			}
		}

		WorldHolder.buffers.draw();

		Symbol symbol = world.getActiveSymbol();

		// render HUD
		if (symbol != null) {
			ScreenRenderer.setSprite(Sprites.TOP);

			ScreenRenderer.centerAt(0, 1);
			ScreenRenderer.offset(-240, -120);
			if (ScreenRenderer.box(480, 120)) {
				isMapFocused = false;
			}

			ScreenRenderer.offset(300, 20);
			if (ScreenRenderer.button("END", 2, 38, 80, true)) {
				new EndTurnPacket().broadcast();
			}

			ScreenRenderer.setSprite(symbol.getSprite());
			ScreenRenderer.setOffset(-220, -98);
			ScreenRenderer.box(80, 80);

			Hotbar.draw(focused, world, symbol);
		}

		ScreenRenderer.centerAt(-1, 1);
		ScreenRenderer.setOffset(0, -40);

		String net = "";

		if (UserGroup.instance != null) {
			net = " " + UserGroup.instance.relay.toString();
		}

		ScreenRenderer.text(Main.window.profiler.getFrameRate() + " FPS" + net, 30);

	}

	@Override
	public void onKey(int key, int action, int mods) {

		if (interactor != null) {
			interactor.onKey(key, action, mods);
			return;
		}

		if(action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_M) {
			world.setOverlay(new MapOverlay());
		}

		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_B) {
			ScreenStack.open(new BuildScreen(world));
		}

		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_TAB && world.isActiveSymbol()) {
			new EndTurnPacket().broadcast();
		}

		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_BACKSPACE) {
			world.getManager().undo();
		}
	}

	@Override
	public void onClick(int button, int action, int mods) {

		if (!isMapFocused || button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
			return;
		}

		Input input = Main.window.input();

		int x = input.getMouseMapX(world.getView());
		int y = input.getMouseMapY(world.getView());

		if (interactor != null) {
			interactor.onClick(button, action, mods, x, y);
			return;
		}

		if(button == GLFW.GLFW_MOUSE_BUTTON_1 || button == GLFW.GLFW_MOUSE_BUTTON_2) {
			if (world.isPositionValid(x, y)) {

				if (action == GLFW.GLFW_PRESS) {
					Entity entity = world.getEntity(x, y);
					ClickEvent event = new ClickEvent(button, action);

					if (entity != null) {
						entity.onInteract(world, x, y, event);
					} else {
						world.getTileState(x, y).getTile().onInteract(world, x, y, event);
					}
				}

			}
		}
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

}
