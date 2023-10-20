package net.darktree.warzone.world;

import net.darktree.warzone.screen.PlayScreen;
import net.darktree.warzone.screen.ScreenStack;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.NbtHelper;
import net.darktree.warzone.util.Util;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class WorldSave implements Comparable<WorldSave> {

	private final Path path;
	private final File meta, map;
	private final String name;
	private final String code;
	private final long time;
	private final CompoundTag nbt;

	protected WorldSave(Path path, File map, File meta, CompoundTag nbt) {
		this.path = path;
		this.meta = meta;
		this.map = map;
		this.name = nbt.getString("name");
		this.code = nbt.getString("code");
		this.time = nbt.getLong("time");
		this.nbt = nbt;
	}

	public static WorldSave read(Path path) {
		File meta = path.resolve("meta.dat").toFile();
		File map = path.resolve("map.dat").toFile();

		if (!meta.canRead() || !map.canRead()) {
			Logger.error("Unable to read save data for: ", path);
			return null;
		}

		CompoundTag nbt = NbtHelper.readFile(meta);

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

	public boolean load() {
		CompoundTag nbt = NbtHelper.readFile(map);

		if (nbt == null) {
			Logger.error("Unable to read save map state!");
			return false;
		}

		try {
			World world = World.load(nbt);
			ScreenStack.closeAll();
			ScreenStack.open(new PlayScreen(this, world));
//			ScreenStack.open(new EditScreen(world));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public void delete() {
		Util.deleteDirectory(path.toFile(), true);
	}

	public boolean save(World world) {
		nbt.putLong("time", Instant.now().getEpochSecond());
		CompoundTag worldNbt = new CompoundTag();
		world.toNbt(worldNbt);
		return NbtHelper.writeFile(meta, nbt) && NbtHelper.writeFile(map, worldNbt);
	}

	@Override
	public int compareTo(@NotNull WorldSave other) {
		if (other.time == this.time) return 0;
		return (other.time > this.time) ? 1 : -1;
	}

}
