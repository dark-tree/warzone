package net.darktree.warzone.screen;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.client.sound.SoundSystem;
import net.darktree.warzone.client.text.Text;
import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.client.window.input.KeyEvent;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.network.UserGroup;
import net.darktree.warzone.network.packet.EndTurnPacket;
import net.darktree.warzone.screen.hotbar.Hotbar;
import net.darktree.warzone.screen.hotbar.HotbarConstruction;
import net.darktree.warzone.screen.hotbar.HotbarOverview;
import net.darktree.warzone.screen.menu.PauseMenuScreen;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.WorldSave;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.overlay.MapOverlay;
import org.lwjgl.glfw.GLFW;

public class PlayScreen extends WorldScreen {

	private final Hotbar hotbar = new Hotbar(Sprites.HOTBAR_PLAY).left(new HotbarOverview()).right(new HotbarConstruction());
	public static final Text TEXT_END = Text.translated("gui.end");
	private final WorldSave save;

	public PlayScreen(WorldSave save, World world) {
		super(world);
		this.save = save;
		SoundSystem.stopMusic();
	}

	@Override
	public void draw(boolean focused) {
		super.draw(focused);

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

			hotbar.draw(focused, world, symbol);
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
	public void onWorldKey(KeyEvent event) {
		if (event.isPressed(GLFW.GLFW_KEY_M)) {
			world.getView().setOverlay(new MapOverlay());
		}

		if (event.isPressed(GLFW.GLFW_KEY_B)) {
			ScreenStack.open(new BuildScreen(world, true));
		}

		if (event.isPressed(GLFW.GLFW_KEY_TAB) && world.isActiveSymbol()) {
			new EndTurnPacket().broadcast();
		}

		if (event.isPressed(GLFW.GLFW_KEY_BACKSPACE)) {
			world.getManager().undo();
		}
	}

	@Override
	public void onWorldClick(ClickEvent event, int x, int y) {
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
	public void onWorldEscape() {
		ScreenStack.open(new PauseMenuScreen(save, world));
	}

}
