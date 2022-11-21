package net.darktree.warzone;

import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.client.render.image.Font;
import net.darktree.warzone.client.render.vertex.Renderer;
import net.darktree.warzone.client.sound.SoundSystem;
import net.darktree.warzone.client.window.Window;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.network.Packets;
import net.darktree.warzone.network.UserGroup;
import net.darktree.warzone.screen.BuildScreen;
import net.darktree.warzone.screen.PlayScreen;
import net.darktree.warzone.screen.ScreenStack;
import net.darktree.warzone.screen.interactor.OwnEditInteractor;
import net.darktree.warzone.screen.interactor.SetEditInteractor;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.Resources;
import net.darktree.warzone.util.Util;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.WorldHolder;
import net.darktree.warzone.world.action.Actions;
import net.darktree.warzone.world.action.manager.ActionManager;
import net.darktree.warzone.world.tile.tiles.Tiles;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;
import org.lwjgl.Version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

		Util.runAsync(() -> {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

			try {
				while (true) {
					console(reader);
				}
			} catch (Exception ignored) {
				Logger.warn("Console thread crashed!");
			}
		}, "Console");

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

	private static void console(BufferedReader reader) throws IOException {
		String line = reader.readLine();

		if (line.startsWith("town ")) {
			String[] parts = line.split(" ");
			int i = Integer.parseInt(parts[1]);

			Main.runSynced(() -> {
				PlayScreen.setInteractor(new OwnEditInteractor(Symbol.fromIndex((byte) i), WorldHolder.world));
			});
		}

		if (line.startsWith("tset ")) {
			String[] parts = line.split(" ");

			Main.runSynced(() -> {
				PlayScreen.setInteractor(new SetEditInteractor(Registries.TILES.getElement(parts[1]).getDefaultVariant(), WorldHolder.world));
			});
		}

		if (line.startsWith("tput ")) {
			String[] parts = line.split(" ");
			int x = Integer.parseInt(parts[1]);
			int y = Integer.parseInt(parts[2]);

			Main.runSynced(() -> {
				WorldHolder.world.addEntity(Registries.ENTITIES.getElement(parts[3]).create(WorldHolder.world, x, y));
			});
		}

		if (line.startsWith("mkplayer ")) {
			String[] parts = line.split(" ");
			int s = Integer.parseInt(parts[1]);

			WorldHolder.world.defineCountry(Symbol.fromIndex((byte) s));
			Logger.warn("The world needs to be re-loaded for this change to fully apply!");
		}

		if (line.startsWith("mkmap ")) {
			String[] parts = line.split(" ");
			int w = Integer.parseInt(parts[1]);
			int h = Integer.parseInt(parts[2]);

			Main.runSynced(() -> {
				WorldHolder.setWorld(new World(w, h));
				WorldHolder.world.loadTiles(pos -> Tiles.EMPTY.getDefaultVariant());
				ScreenStack.closeAll();
				ScreenStack.open(new PlayScreen(WorldHolder.world));
			});
		}

		if (line.startsWith("load ")) {
			Main.runSynced(() -> {
				try {
					World.load((CompoundTag) NBTUtil.read(line.split(" ")[1], true).getTag());
					ScreenStack.closeAll();
					ScreenStack.open(new PlayScreen(WorldHolder.world));
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}

		if (line.startsWith("save ")) {
			Main.runSynced(() -> {
				CompoundTag tag = new CompoundTag();
				WorldHolder.world.toNbt(tag);
				try {
					NBTUtil.write(tag, line.split(" ")[1], true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}

		if (line.equals("rstop")) {
			UserGroup.closeAll();
		}

		if (line.startsWith("self ")) {
			WorldHolder.world.self = Symbol.fromIndex((byte) Integer.parseInt(line.split(" ")[1]));
		}

		if (line.equals("exit")) {
			return;
		}

		if (line.equals("rmake")) {
			UserGroup.make("localhost", group -> {
				Logger.info("Group made! " + group.id);
				WorldHolder.world.manager = new ActionManager.Host(WorldHolder.world);
			}, Logger::error);
		}

		if (line.startsWith("rjoin ")) {
			int gid = Integer.parseInt(line.split(" ")[1]);

			UserGroup.join("localhost", gid, group -> {
				Logger.info("Group joined! " + group.id);
				WorldHolder.world.manager = new ActionManager.Client(WorldHolder.world);
			}, Logger::error);
		}
	}
}

