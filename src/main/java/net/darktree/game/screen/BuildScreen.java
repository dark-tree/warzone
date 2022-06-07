package net.darktree.game.screen;

import net.darktree.Main;
import net.darktree.core.client.Colors;
import net.darktree.core.client.Sprites;
import net.darktree.core.client.render.Alignment;
import net.darktree.core.client.render.Screen;
import net.darktree.core.client.render.ScreenRenderer;
import net.darktree.core.client.window.input.MouseButton;
import net.darktree.core.world.World;
import net.darktree.game.buildings.BuildingConfigRegistry;
import net.darktree.game.interactor.BuildInteractor;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class BuildScreen extends Screen {

	private final World world;

	public BuildScreen(World world) {
		this.world = world;
	}

	private void option(BuildingConfigRegistry.Config entry, World world) {
		if (ScreenRenderer.isMouseOver(400, 100)) {
			ScreenRenderer.setColor(Colors.BUTTON_HOVER);
			description(entry.description, entry.type.value);

			if (Main.window.input().isButtonPressed(MouseButton.LEFT)) {
				ScreenRenderer.setColor(Colors.BUTTON_PRESSED);
			}

			if (Main.window.input().hasClicked()) {
				PlayScreen.setInteractor(new BuildInteractor(entry.type, world));
				this.close();
			}
		}

		ScreenRenderer.setAlignment(Alignment.LEFT);
		ScreenRenderer.setSprite(entry.type.sprite);
		ScreenRenderer.box(100, 100);
		ScreenRenderer.offset(100, 6);
		ScreenRenderer.text(entry.type.value + "m", 30);
		ScreenRenderer.offset(0, 54);
		ScreenRenderer.text(entry.name, 30);
		ScreenRenderer.offset(-100, -180);
		ScreenRenderer.setColor(Colors.NONE);
	}

	private void description(String text, int value) {
		text(-100, 120, text + "\n\nCOST: " + value + "m", Alignment.LEFT);
	}

	@Override
	public void draw(boolean focused) {

		ScreenRenderer.centerAt(-1, -1);
		ScreenRenderer.setSprite(Sprites.NULL);
		ScreenRenderer.setColor(Colors.SCREEN_SEPARATOR);
		ScreenRenderer.box(Main.window.width() * 2, Main.window.height() * 2);

		ScreenRenderer.centerAt(0, 0);
		ScreenRenderer.setColor(Colors.NONE);
		ScreenRenderer.setSprite(Sprites.BUILD);

		text(0, 310, "SELECT A BUILDING", Alignment.CENTER);
		text(0, 270, "PAGE 1/1", Alignment.CENTER);
		box(-650, -400, 1300, 800);

		ScreenRenderer.setOffset(-550, 60);

		List<BuildingConfigRegistry.Config> entries = BuildingConfigRegistry.getEntries();

		for (int i = 0; i < 4 && i < entries.size(); i ++) {
			option(entries.get(i), world);
		}

		ScreenRenderer.setOffset(650 - 300, -400 + 100);
		ScreenRenderer.button("<", 1, 50, 50, false);
		ScreenRenderer.offset(44, 0);
		ScreenRenderer.button(">", 1, 50, 50, false);

	}

	@Override
	public void onKey(int key, int action, int mods) {
		if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
			this.close();
		}
	}

}
