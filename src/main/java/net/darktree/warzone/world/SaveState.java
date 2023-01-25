package net.darktree.warzone.world;

import net.darktree.warzone.screen.PlayScreen;
import net.darktree.warzone.screen.ScreenStack;
import net.darktree.warzone.util.NbtAccess;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.StringTag;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class SaveState {

	private final String name;
	private final String code;
	private final long time;
	private final CompoundTag nbt;

	private String getDefaultedString(CompoundTag nbt, String key) {
		return Optional.ofNullable(nbt.getStringTag(key)).orElse(new StringTag("*UNSET*")).getValue();
	}

	public SaveState(CompoundTag nbt) {
		CompoundTag info = NbtAccess.getTag("info", nbt);
		this.name = getDefaultedString(info, "name");
		this.code = getDefaultedString(info, "code");
		this.time = info.getLong("time");
		this.nbt = nbt;
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
		World world = World.load(nbt);
		ScreenStack.closeAll();
		ScreenStack.open(new PlayScreen(world));
	}

}
