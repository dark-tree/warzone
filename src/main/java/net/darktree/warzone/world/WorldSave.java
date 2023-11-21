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
import java.time.format.DateTimeFormatter;

public class WorldSave implements Comparable<WorldSave> {

	private final Path path;
	private final File meta, map;
	private final WorldInfo info;

	protected WorldSave(Path path, File map, File meta, WorldInfo info) {
		this.path = path;
		this.meta = meta;
		this.map = map;
		this.info = info;
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

		try {
			WorldInfo info = WorldInfo.load(nbt);
			return new WorldSave(path, map, meta, info);
		} catch (Exception e) {
			Logger.error("Failed to load save metadata for: " + path, ", ", e.getMessage());
			return null;
		}
	}

	public String getTime(DateTimeFormatter formatter) {
		return info.getModifyTime(formatter);
	}

	public String getName() {
		return info.getMapName();
	}

	public String getCode() {
		return info.getCodeName();
	}

	public boolean load() {
		CompoundTag nbt = NbtHelper.readFile(map);

		if (nbt == null) {
			Logger.error("Unable to read save map state!");
			return false;
		}

		try {
			WorldAccess world = WorldAccess.load(info, nbt);
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

	public boolean save(WorldAccess world) {
		CompoundTag worldNbt = new CompoundTag();
		world.toNbt(worldNbt);

		CompoundTag metaNbt = new CompoundTag();
		info.toNbt(metaNbt);
		return NbtHelper.writeFile(meta, metaNbt) && NbtHelper.writeFile(map, worldNbt);
	}

	@Override
	public int compareTo(@NotNull WorldSave save) {
		return Long.compare(save.info.modified, info.modified);
	}

}
