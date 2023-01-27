package net.darktree.warzone;

import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.client.render.image.Font;
import net.darktree.warzone.client.sound.SoundSystem;
import net.darktree.warzone.client.text.Text;
import net.darktree.warzone.client.window.Window;
import net.darktree.warzone.country.Resource;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.country.upgrade.Upgrade;
import net.darktree.warzone.country.upgrade.Upgrades;
import net.darktree.warzone.network.Packets;
import net.darktree.warzone.network.UserGroup;
import net.darktree.warzone.screen.BuildScreen;
import net.darktree.warzone.screen.PlayScreen;
import net.darktree.warzone.screen.PopupScreen;
import net.darktree.warzone.screen.ScreenStack;
import net.darktree.warzone.screen.interactor.OwnEditInteractor;
import net.darktree.warzone.screen.interactor.SetEditInteractor;
import net.darktree.warzone.screen.menu.MainMenuScreen;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.Resources;
import net.darktree.warzone.util.Util;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.WorldHolder;
import net.darktree.warzone.world.action.Actions;
import net.darktree.warzone.world.action.manager.ActionManager;
import net.darktree.warzone.world.tile.tiles.Tiles;
import org.lwjgl.Version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.lwjgl.opengl.GL32.glClearColor;

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

		long start = System.currentTimeMillis();

		window = Window.init(800, 500, "Warzone Open Rules | Java Edition");

		Packets.load();
		SoundSystem.enable();

		// FIXME let's not
		Util.load(Sounds.class);
		Util.load(Actions.class);
		Util.load(Upgrades.class);
		Util.load(net.darktree.warzone.country.Resources.class);

		// Set the clear color, evil blue from LT3D (patent pending)
		glClearColor(0.01f, 0.66f, 0.92f, 0.00f);

		WorldHolder.setWorld(new World(8, 8));
		WorldHolder.world.loadTiles(pos -> Tiles.EMPTY.getDefaultVariant());

		// load and set font
		Font scribble = Font.load("scribble");
		game = new Game();

		ScreenStack.open(new MainMenuScreen());

		BuildScreen.register(Tiles.WAREHOUSE);
		BuildScreen.register(Tiles.FACTORY);
		BuildScreen.register(Tiles.PARLIAMENT);

		Logger.info("System ready, took ", System.currentTimeMillis() - start, "ms!");

		Util.runAsync(() -> {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

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
			Window.alert("Main thread has thrown an exception and crashed!\nThe application will be terminated!", "Game has crashed!");
			Logger.fatal("Main thread has thrown an exception and crashed!");
			e.printStackTrace();
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
				PlayScreen.setInteractor(new SetEditInteractor(Registries.TILES.byKey(parts[1]).value().getDefaultVariant(), WorldHolder.world));
			});
		}

		if (line.startsWith("tput ")) {
			String[] parts = line.split(" ");
			int x = Integer.parseInt(parts[1]);
			int y = Integer.parseInt(parts[2]);

			Main.runSynced(() -> {
				WorldHolder.world.addEntity(Registries.ENTITIES.byKey(parts[3]).value().create(WorldHolder.world, x, y));
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
				ScreenStack.open(new PlayScreen(null, WorldHolder.world));
			});
		}

		if (line.startsWith("give ")) {
			String[] parts = line.split(" ");
			int count = Integer.parseInt(parts[2]);

			Main.runSynced(() -> {
				Resource.Quantified resource = Registries.RESOURCES.byKey(parts[1]).value().quantify(count);
				WorldHolder.world.getCountry(WorldHolder.world.getCurrentSymbol()).addResource(resource);
				Logger.info("Done!");
			});
		}

		if (line.startsWith("upgrade ")) {
			String[] parts = line.split(" ");

			Main.runSynced(() -> {
				Upgrade<?> upgrade = Registries.UPGRADES.byKey(parts[1]).value();
				WorldHolder.world.getCountry(WorldHolder.world.getCurrentSymbol()).upgrades.grant(upgrade);
				Logger.info("Done!");
			});
		}

		if (line.equals("rstop")) {
			UserGroup.instance.close();
		}

		if (line.startsWith("self ")) {
			WorldHolder.world.self = Symbol.fromIndex((byte) Integer.parseInt(line.split(" ")[1]));
			Logger.info("Identity set!");
		}

		if (line.startsWith("rmake ")) {
			String[] parts = line.split(" ");
			UserGroup.make(parts[1], group -> {
				WorldHolder.world.manager = new ActionManager.Host(WorldHolder.world);
			}, reason -> {
				ScreenStack.open(new PopupScreen(Text.translated("network.error.closed"), reason.toUpperCase(Locale.ROOT)));
				WorldHolder.world.manager = new ActionManager(WorldHolder.world);
				UserGroup.instance.close();
				UserGroup.instance = null;
			});
		}

		if (line.startsWith("rjoin ")) {
			String[] parts = line.split(" ");
			UserGroup.join(parts[1], Integer.parseInt(parts[2]), group -> {
				WorldHolder.world.manager = new ActionManager.Client(WorldHolder.world);
			}, reason -> {
				ScreenStack.open(new PopupScreen(Text.translated("network.error.closed"), reason.toUpperCase(Locale.ROOT)));
				WorldHolder.world.manager = new ActionManager(WorldHolder.world);
				UserGroup.instance.close();
				UserGroup.instance = null;
			});
		}
	}
}

