package net.darktree.warzone.screen;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.client.sound.SoundSystem;
import net.darktree.warzone.client.text.Text;
import net.darktree.warzone.client.window.Input;
import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.client.window.input.KeyEvent;
import net.darktree.warzone.client.window.input.MouseButton;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.network.UserGroup;
import net.darktree.warzone.network.packet.EndTurnPacket;
import net.darktree.warzone.screen.hotbar.Hotbar;
import net.darktree.warzone.screen.interactor.Interactor;
import net.darktree.warzone.screen.menu.PauseMenuScreen;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.WorldSave;
import net.darktree.warzone.world.WorldView;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.overlay.FearOverlay;
import net.darktree.warzone.world.overlay.MapOverlay;
import net.darktree.warzone.world.overlay.PrincipalityOverlay;
import net.darktree.warzone.world.terrain.ChokepointFinder;
import net.darktree.warzone.world.terrain.DangerFinder;
import org.lwjgl.glfw.GLFW;

public class PlayScreen extends WorldScreen {

	private Interactor interactor = null;

	public static final Text TEXT_END = Text.translated("gui.end");

	public static void setInteractor(Interactor interactor) {
		ScreenStack.asInstance(PlayScreen.class, screen -> screen.interactor = interactor);
	}

	private boolean isMapFocused = true;
	private final WorldSave save;

	public PlayScreen(WorldSave save, World world) {
		super(world);
		this.save = save;
		SoundSystem.stopMusic();
	}

	@Override
	public void draw(boolean focused) {
		super.draw(focused);
		isMapFocused = focused;

		if (interactor != null) {
			interactor.draw(buffers.getEntity(), buffers.getOverlay());

			if (interactor.isClosed()) {
				interactor.close();
				interactor = null;
			}
		}

		buffers.draw();
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
			if (ScreenRenderer.button(TEXT_END, 2, 38, 80, true)) {
				Sounds.PEN_CLICK.play();
				new EndTurnPacket().broadcast();
			}

			ScreenRenderer.setSprite(symbol.getSprite());
			ScreenRenderer.setOffset(-220, -98);
			ScreenRenderer.box(80, 80);

			Hotbar.draw(focused, world, symbol);
		}

		ScreenRenderer.centerAt(-1, 1);
		ScreenRenderer.setOffset(0, -40);

		// draw debug overlay
		StringBuilder builder = new StringBuilder();
		appendDebugInfo(builder);
		ScreenRenderer.text(30, builder);
	}

	private void appendDebugInfo(StringBuilder builder) {
		builder.append(Main.window.profiler.getFrameRate()).append(" FPS");
		builder.append("\nP=").append(SoundSystem.getSourceCount()).append(' ');
		ScreenRenderer.appendDebugInfo(builder);

		if (UserGroup.instance != null) {
			builder.append('\n').append(UserGroup.instance.relay);
		}
	}

	@Override
	public void onKey(KeyEvent event) {
		super.onKey(event);

		if (interactor != null) {
			interactor.onKey(event);
			return;
		}

		if (event.isPressed(GLFW.GLFW_KEY_M)) {
			world.getView().setOverlay(new MapOverlay());
		}

		if (event.isPressed(GLFW.GLFW_KEY_P)) {
			world.getView().setOverlay(new PrincipalityOverlay(world));
		}

		if (event.isPressed(GLFW.GLFW_KEY_O)) {
			world.getView().setOverlay(new FearOverlay(new DangerFinder(Symbol.CROSS, world), new ChokepointFinder(world)));
		}

		if (event.isPressed(GLFW.GLFW_KEY_B)) {
			ScreenStack.open(new BuildScreen(world));
		}

		if (event.isPressed(GLFW.GLFW_KEY_TAB) && world.isActiveSymbol()) {
			new EndTurnPacket().broadcast();
		}

		if (event.isPressed(GLFW.GLFW_KEY_BACKSPACE)) {
			world.getManager().undo();
		}
	}

	@Override
	public void onClick(ClickEvent event) {
		if (!isMapFocused || event.button == MouseButton.MIDDLE) {
			return;
		}

		Input input = Main.window.input();
		int x = input.getMouseMapX(world.getView());
		int y = input.getMouseMapY(world.getView());

		if (interactor != null) {
			interactor.onClick(event, x, y);
			return;
		}

		// every action can be performed with right click, is this intended?

		if((event.isLeftClick() || event.isRightClick()) && world.isPositionValid(x, y)) {
			Entity entity = world.getEntity(x, y);

			if (entity != null) {
				entity.onInteract(world, x, y, event);
			} else {
				world.getTileState(x, y).getTile().onInteract(world, x, y, event);
			}
		}
	}

	@Override
	public void onEscape() {
		if (interactor != null) {
			return;
		}

		ScreenStack.open(new PauseMenuScreen(save, world));
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
