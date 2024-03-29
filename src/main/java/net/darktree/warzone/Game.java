package net.darktree.warzone;

import net.darktree.warzone.client.render.vertex.Renderer;
import net.darktree.warzone.client.sound.SoundSystem;
import net.darktree.warzone.client.text.Language;
import net.darktree.warzone.screen.ComposedScreen;
import net.darktree.warzone.screen.ScreenStack;
import net.darktree.warzone.screen.menu.DecoratedScreen;
import net.darktree.warzone.screen.menu.LoginScreen;
import net.darktree.warzone.screen.menu.MainMenuScreen;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.NbtHelper;
import net.darktree.warzone.util.Resources;
import net.darktree.warzone.world.WorldAccess;
import net.darktree.warzone.world.WorldSave;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class Game {

	private static final File location = Path.of(Resources.path() + "/state/game.dat").toFile();
	private CompoundTag state;
	private WorldAccess world;
	public Language lang;

	public Game() {
		lang = Language.load("en_US");
	}

	public Optional<WorldAccess> getWorld() {
		return Optional.ofNullable(world);
	}

	public void setWorld(WorldAccess world) {
		this.world = world;
	}

	public List<WorldSave> getSaves() {
		return Resources.listing("state/saves").parallel().map(WorldSave::read).filter(Objects::nonNull).sorted().toList();
	}

	public String getUsername() {
		return state.getString("player");
	}

	public void setUsername(String value) {
		state.putString("player", value);
		flushGlobalState();
	}

	public void draw() {
		SoundSystem.tick();
		Renderer.clear();
		ScreenStack.draw();

		Renderer.swap();
	}

	public void initialize() {
		state = NbtHelper.readFile(location);

		if (state == null) {
			state = new CompoundTag();
			ScreenStack.open(new ComposedScreen(new DecoratedScreen(), new LoginScreen()));
			return;
		}

		ScreenStack.open(new ComposedScreen(new DecoratedScreen(), new MainMenuScreen()));
		Logger.info("Player name set to: '", getUsername(), "'");
	}

	private void flushGlobalState() {
		try {
			location.getParentFile().mkdirs();
			location.createNewFile();
			NBTUtil.write(state, location, true);
		} catch (Exception ignore) {
			Logger.error("Failed to save global state!");
		}
	}

}
