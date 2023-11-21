package net.darktree.warzone;

import com.google.common.collect.ImmutableList;
import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.client.render.GLManager;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.client.render.image.Font;
import net.darktree.warzone.client.render.vertex.Renderer;
import net.darktree.warzone.client.sound.SoundSystem;
import net.darktree.warzone.client.window.Window;
import net.darktree.warzone.country.Resource;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.country.controller.LocalController;
import net.darktree.warzone.country.controller.MachineController;
import net.darktree.warzone.country.controller.NullController;
import net.darktree.warzone.country.upgrade.Upgrades;
import net.darktree.warzone.network.Packets;
import net.darktree.warzone.network.UserGroup;
import net.darktree.warzone.network.packet.EndTurnPacket;
import net.darktree.warzone.screen.BuildScreen;
import net.darktree.warzone.screen.PlayScreen;
import net.darktree.warzone.screen.ScreenStack;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.Profiler;
import net.darktree.warzone.util.Resources;
import net.darktree.warzone.util.Util;
import net.darktree.warzone.world.WorldAccess;
import net.darktree.warzone.world.WorldInfo;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.action.Actions;
import net.darktree.warzone.world.entity.Entities;
import net.darktree.warzone.world.tile.tiles.Tiles;
import org.lwjgl.Version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

	public static Window window;
	public static Game game;

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
		Logger.info("Using Java ", Runtime.version().feature(), " (", Runtime.version(), ")");

		Profiler profiler = Profiler.start();

		window = Window.init(800, 500, "Warzone Open Rules | Java Edition");

		// load and set font
		ScreenRenderer.setFont(Font.load("scribble"));

		// Set the clear color, evil blue from LT3D (patent pending)
		GLManager.useColor(0.01f, 0.66f, 0.92f);
		Renderer.clear();
		ScreenRenderer.flush();
		drawLoadingScreen();
		Renderer.swap();
		ScreenRenderer.initializeQuadPipeline();

		Packets.load();
		SoundSystem.enable();

		// FIXME let's not
		Util.load(Sounds.class);
		Util.load(Actions.class);
		Util.load(Upgrades.class);
		Util.load(Tiles.class);
		Util.load(net.darktree.warzone.country.Resources.class);

		game = new Game();
		game.initialize();

		BuildScreen.register(Entities.WAREHOUSE);
		BuildScreen.register(Entities.FACTORY);
		BuildScreen.register(Entities.PARLIAMENT);
		BuildScreen.register(Entities.FENCE);
		BuildScreen.register(Entities.WALL);
		BuildScreen.register(Entities.BRIDGE);

		profiler.print("System ready, took %sms!");

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		Util.runAsync(() -> {
			while (true) {
				try {
					console(reader);
				} catch (Exception e) {
					Logger.warn("Exception in console thread!");
					e.printStackTrace();
					return;
				}
			}
		}, "Console");

		try {
			loop();
		}catch (Exception e) {
			e.printStackTrace();
			Window.alert("Main thread has thrown an exception and crashed!\nThe application will be terminated!", "Game has crashed!");
			Logger.fatal("Main thread has thrown an exception and crashed!");
		}

		if (UserGroup.instance != null) {
			UserGroup.instance.close();
		}

		window.close();
		SoundSystem.disable();
	}

	private static void loop() {
		while (!window.shouldClose()) {
			game.draw();

			synchronized (tasks) {
				tasks.forEach(Runnable::run);
				tasks.clear();
			}
		}
	}

	private static void drawLoadingScreen() {
		ScreenRenderer.centerAt(-1, -1);
		ScreenRenderer.offset(0, 4);
		ScreenRenderer.text(30, "LOADING...");
		ScreenRenderer.flush();
	}

	private static void console(BufferedReader reader) throws IOException {
		String line = reader.readLine();
		WorldAccess world = game.getWorld().orElse(null);

		if (world != null && line.startsWith("tput ")) {
			String[] parts = line.split(" ");
			int x = Integer.parseInt(parts[1]);
			int y = Integer.parseInt(parts[2]);

			Main.runSynced(() -> {
				WorldSnapshot snapshot = world.getTrackingWorld();
				snapshot.addEntity(Registries.ENTITIES.byKey(parts[3]).value().create(snapshot, x, y));
			});
		}

		if (world != null && line.startsWith("mkplayer ")) {
			String[] parts = line.split(" ");
			int s = Integer.parseInt(parts[1]);

			world.getTrackingWorld().defineCountry(Symbol.fromIndex((byte) s));
			Logger.warn("The world needs to be re-loaded for this change to fully apply!");
		}

		if (line.startsWith("mkmap ")) {
			String[] parts = line.split(" ");
			int w = Integer.parseInt(parts[1]);
			int h = Integer.parseInt(parts[2]);

			Main.runSynced(() -> {
				WorldInfo info = new WorldInfo(w, h, "NEW0", "Untitled Map", ImmutableList.of(Symbol.CROSS));

				game.setWorld(new WorldAccess(info, new WorldSnapshot(info, null)));
				ScreenStack.closeAll();
				ScreenStack.open(new PlayScreen(null, game.getWorld().orElseThrow()));
			});
		}

		// TODO this is broken now lol
		if (world != null && line.startsWith("give ")) {
			String[] parts = line.split(" ");
			int count = Integer.parseInt(parts[2]);

			Main.runSynced(() -> {
				Resource.Quantified resource = Registries.RESOURCES.byKey(parts[1]).value().quantify(count);
				world.getTrackingWorld().getCountry(world.getCurrentSymbol()).addResource(resource);
				Logger.info("Done!");
			});
		}

		if (line.equals("rstop")) {
			UserGroup.instance.close();
		}

		if (world != null && line.startsWith("player ")) {
			String[] parts = line.split(" ");
			Symbol symbol = Symbol.fromIndex((byte) Integer.parseInt(parts[1]));
			String type = parts[2];

			world.getTrackingWorld().getCountry(symbol).controller = switch (type) {
				case "self" -> new LocalController();
				case "null" -> new NullController();
				case "ai" -> new MachineController();
				default -> throw new RuntimeException("Invalid player type, expected 'self', 'null' or 'ai'!");
			};

			Logger.info("Identity set!");
		}

		if (world != null && line.equals("next")) {
			new EndTurnPacket().broadcast();
		}

//		if (world != null && line.startsWith("rmake ")) {
//			String[] parts = line.split(" ");
//			UserGroup.make(parts[1], group -> {
//				world.manager = new ActionManager.Host(world);
//			}, reason -> {
//				ScreenStack.open(new AcceptScreen(Text.translated("network.error.closed"), reason.toUpperCase(Locale.ROOT)));
//				world.manager = new ActionManager(world);
//				UserGroup.instance.close();
//				UserGroup.instance = null;
//			});
//		}
//
//		if (world != null && line.startsWith("rjoin ")) {
//			String[] parts = line.split(" ");
//			UserGroup.join(parts[1], Integer.parseInt(parts[2]), group -> {
//				world.manager = new ActionManager.Client(world);
//			}, reason -> {
//				ScreenStack.open(new AcceptScreen(Text.translated("network.error.closed"), reason.toUpperCase(Locale.ROOT)));
//				world.manager = new ActionManager(world);
//				UserGroup.instance.close();
//				UserGroup.instance = null;
//			});
//		}
	}
}

