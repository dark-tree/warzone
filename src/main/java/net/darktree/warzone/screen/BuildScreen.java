package net.darktree.warzone.screen;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.Alignment;
import net.darktree.warzone.client.render.Screen;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.client.window.input.MouseButton;
import net.darktree.warzone.screen.interactor.BuildInteractor;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.building.Building;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class BuildScreen extends Screen {

	private final World world;
	private static final List<EntryConfig> entries = new ArrayList<>();

	private int page = 0;
	private final int pages;

	public BuildScreen(World world) {
		this.world = world;
		this.pages = (int) Math.ceil(entries.size() / 3.0f);
	}

	private void option(EntryConfig entry, World world, int materials) {
		final boolean enough = entry.type.value <= materials;

		if (ScreenRenderer.isMouseOver(400, 100)) {
			ScreenRenderer.setColor(Colors.BUTTON_HOVER);
			description(entry.description, entry.type.value);

			if (Main.window.input().isButtonPressed(MouseButton.LEFT)) {
				ScreenRenderer.setColor(Colors.BUTTON_PRESSED);
			}

			if (enough && Main.window.input().hasClicked()) {
				PlayScreen.setInteractor(new BuildInteractor(entry.type, world));
				this.close();
			}
		}

		ScreenRenderer.setAlignment(Alignment.LEFT);
		ScreenRenderer.setSprite(entry.type.icon);
		ScreenRenderer.box(100, 100);
		ScreenRenderer.offset(100, 6);

		if (!enough) ScreenRenderer.setColor(Colors.TOO_EXPENSIVE);
		ScreenRenderer.text(entry.type.value + "m", 30);
		ScreenRenderer.setColor(Colors.NONE);
		ScreenRenderer.offset(0, 54);
		ScreenRenderer.text(entry.name, 30);
		ScreenRenderer.offset(-100, -180);
		ScreenRenderer.setColor(Colors.NONE);
	}

	private void description(String text, int value) {
		text(-100, 130, text + "\n\nCOST: " + value + "m", Alignment.LEFT);
	}

	@Override
	public void draw(boolean focused) {

		drawTitledScreen("SELECT A BUILDING", "PAGE " + (page + 1) + "/" + pages, Sprites.BUILD, 1300, 800);

		final int materials = world.getCountry(world.getCurrentSymbol()).getTotalMaterials();
		final int start = this.page * 3;
		final int end = Math.min(start + 3, entries.size());

		ScreenRenderer.offset(100, 100);
		ScreenRenderer.setAlignment(Alignment.LEFT);
		ScreenRenderer.text("MATERIALS: " + materials, 30);
		ScreenRenderer.setOffset(-540, 70);

		for (int i = start; i < end; i ++) {
			option(entries.get(i), world, materials);
		}

		ScreenRenderer.setOffset(650 - 250, -400 + 100);
		if (ScreenRenderer.button(Sprites.BUTTON_LEFT, 64, 64, page > 0)) {
			this.page --;
			Sounds.PAGE.play();
		}

		ScreenRenderer.offset(70, 0);
		if (ScreenRenderer.button(Sprites.BUTTON_RIGHT, 64, 64, page < (pages - 1))) {
			this.page ++;
			Sounds.PAGE.play();
		}

	}

	@Override
	public void onKey(int key, int action, int mods) {
		if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
			this.close();
		}
	}

	public static void register(Building.Type type, String name, String description) {
		entries.add(new EntryConfig(type, name, description));
	}

	public static class EntryConfig {
		public final Building.Type type;
		public final String name;
		public final String description;

		public EntryConfig(Building.Type type, String name, String description) {
			this.type = type;
			this.name = name;
			this.description = description;
		}
	}

}
