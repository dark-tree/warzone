package net.darktree;

import net.darktree.core.client.render.Screen;
import net.darktree.core.client.render.image.Font;
import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.sound.SoundSystem;
import net.darktree.core.client.window.Window;
import net.darktree.core.util.Logger;
import net.darktree.core.util.Resources;
import net.darktree.core.world.World;
import net.darktree.core.world.WorldHolder;
import net.darktree.game.buildings.BuildingConfigRegistry;
import net.darktree.game.screen.PlayScreen;
import net.darktree.game.tiles.Tiles;
import org.lwjgl.Version;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;

import static org.lwjgl.opengl.GL32.glClearColor;

public class Main {

	public static Stack<Screen> screens = new Stack<>();
	public static Window window;

	public static void main(String[] args) {
		Logger.info("Current working directory: ", Resources.path());
		Logger.info("Using LWJGL ", Version.getVersion());

		long start = System.currentTimeMillis();

		window = Window.init(800, 500, "Game");

		SoundSystem.enable();

//		Sounds.TEST_SONG.play().setLoop(true).setVolume(0.8f);

		// Set the clear color, evil blue from LT3D (patent pending)
		glClearColor(0.01f, 0.66f, 0.92f, 0.00f);

		WorldHolder.world = new World(8, 8);
		WorldHolder.world.loadTiles(pos -> Tiles.EMPTY.getDefaultVariant());

		// load and set font
		Font scribble = Font.load("scribble");

		Logger.info("System ready, took ", System.currentTimeMillis() - start, "ms!");

		screens.push(new PlayScreen(WorldHolder.world));

		BuildingConfigRegistry.register(Tiles.FACTORY, "FACTORY", "ALLOWS YOU TO PRODUCE\nAMMUNITION AND ARMORS\nFOR YOUR UNITS.");

		try {
			loop();
		}catch (Exception e) {
			Window.alert("Main thread has thrown an exception and crashed!\nThe application will be terminated!", "Game has crashed!");
			Logger.fatal("Main thread has thrown an exception and crashed!");
			e.printStackTrace();
		}

		window.close();
		SoundSystem.disable();
	}

	public static Queue<Screen> pendingScreens = new ArrayDeque<>();

	private static void loop() {
		while (!window.shouldClose()) {
			screens.forEach(Screen::draw);
			screens.removeIf(Screen::isClosed);

			pendingScreens.forEach(screen -> screens.push(screen));
			pendingScreens.clear();

			Renderer.next();
		}
	}

}

