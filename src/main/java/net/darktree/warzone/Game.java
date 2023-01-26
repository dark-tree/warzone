package net.darktree.warzone;

import net.darktree.warzone.client.render.vertex.Renderer;
import net.darktree.warzone.client.text.Language;
import net.darktree.warzone.screen.ScreenStack;
import net.darktree.warzone.util.NbtAccess;
import net.darktree.warzone.util.Resources;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.WorldHolder;
import net.darktree.warzone.world.WorldSave;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Game {

	public Language lang;

	public Game() {
		lang = Language.load("en_US");
	}

	public Optional<World> getWorld() {
		return Optional.ofNullable(WorldHolder.world);
	}

	public List<WorldSave> getSaves() {
		return Resources.listing("state/saves").parallel().map(Path::toFile).map(NbtAccess::readFile).filter(Objects::nonNull).map(WorldSave::new).toList();
	}

	public void draw() {
		ScreenStack.draw();
		Renderer.next();
	}

}
