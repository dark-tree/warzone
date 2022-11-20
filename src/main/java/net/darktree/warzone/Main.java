package net.darktree.warzone;

import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.client.render.image.Font;
import net.darktree.warzone.client.render.vertex.Renderer;
import net.darktree.warzone.client.sound.SoundSystem;
import net.darktree.warzone.client.window.Window;
import net.darktree.warzone.network.Packets;
import net.darktree.warzone.network.UserGroup;
import net.darktree.warzone.screen.BuildScreen;
import net.darktree.warzone.screen.PlayScreen;
import net.darktree.warzone.screen.ScreenStack;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.Resources;
import net.darktree.warzone.util.Util;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.WorldHolder;
import net.darktree.warzone.world.action.Actions;
import net.darktree.warzone.world.tile.tiles.Tiles;
import org.lwjgl.Version;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.opengl.GL32.glClearColor;

public class Main {

	public static Window window;
	private static final List<Runnable> tasks = Collections.synchronizedList(new ArrayList<>());

	/**
	 * Run a piece of code on the main thread
	 */
	public static void runSynced(Runnable runnable) {
		synchronized (tasks) {
			tasks.add(runnable);
		}
	}

	public static void main(String[] args) {
		Logger.info("Current working directory: ", Resources.path());
		Logger.info("Using LWJGL ", Version.getVersion());

		long start = System.currentTimeMillis();

		window = Window.init(800, 500, "Game");

		Packets.load();
		SoundSystem.enable();
		Util.load(Sounds.class);
		Util.load(Actions.class);

		// Set the clear color, evil blue from LT3D (patent pending)
		glClearColor(0.01f, 0.66f, 0.92f, 0.00f);

		WorldHolder.setWorld(new World(8, 8));
		WorldHolder.world.loadTiles(pos -> Tiles.EMPTY.getDefaultVariant());

		// load and set font
		Font scribble = Font.load("scribble");

		Logger.info("System ready, took ", System.currentTimeMillis() - start, "ms!");

		ScreenStack.open(new PlayScreen(WorldHolder.world));

		BuildScreen.register(Tiles.FACTORY, "FACTORY", "ALLOWS YOU TO PRODUCE\nAMMUNITION AND ARMORS\nFOR YOUR UNITS.");

		try {
			loop();
		}catch (Exception e) {
			Window.alert("Main thread has thrown an exception and crashed!\nThe application will be terminated!", "Game has crashed!");
			Logger.fatal("Main thread has thrown an exception and crashed!");
			e.printStackTrace();
		}

		UserGroup.closeAll();
		window.close();
		SoundSystem.disable();
	}

	private static void loop() {
		while (!window.shouldClose()) {
			ScreenStack.draw();
			Renderer.next();

			synchronized (tasks) {
				tasks.forEach(Runnable::run);
				tasks.clear();
			}
		}
	}

}

