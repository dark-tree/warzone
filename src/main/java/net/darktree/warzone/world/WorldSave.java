package net.darktree.warzone.world;

import net.darktree.warzone.screen.PlayScreen;
import net.darktree.warzone.screen.ScreenStack;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.NbtAccess;
import net.darktree.warzone.util.Util;
import net.querz.nbt.tag.CompoundTag;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class WorldSave {

	private final Path path;
	private final File meta, map;
	private final String name;
	private final String code;
	private final long time;

	protected WorldSave(Path path, File map, File meta, CompoundTag nbt) {
		this.path = path;
		this.meta = meta;
		this.map = map;
		this.name = nbt.getString("name");
		this.code = nbt.getString("code");
		this.time = nbt.getLong("time");
	}

	public static WorldSave read(Path path) {
		File meta = path.resolve("meta.dat").toFile();
		File map = path.resolve("map.dat").toFile();

		if (!meta.canRead() || !map.canRead()) {
			Logger.error("Unable to read save data for: ", path);
			return null;
		}

		CompoundTag nbt = NbtAccess.readFile(meta);

		if (nbt == null) {
			Logger.error("Unable to read save metadata for: ", path);
			return null;
		}

		return new WorldSave(path, map, meta, nbt);
	}

	public String getTime(DateTimeFormatter formatter) {
		return LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC).format(formatter);
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public void load() {
		CompoundTag nbt = NbtAccess.readFile(map);

		if (nbt == null) {
			Logger.error("Unable to read save map state!");
			return;
		}

		World world = World.load(nbt);
		ScreenStack.closeAll();
		ScreenStack.open(new PlayScreen(world));
	}

	public void delete() {
		Util.deleteDirectory(path.toFile(), true);
	}

}
