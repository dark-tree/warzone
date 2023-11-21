package net.darktree.warzone.world;

import net.darktree.warzone.country.Symbol;
import net.querz.nbt.tag.ByteTag;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorldInfo {

	public final int width;
	public final int height;
	public final String name;
	public final String code;
	public final long created;
	public final long modified;
	public final List<Symbol> symbols;

	public WorldInfo(int width, int height, String name, String code, long created, long modified, List<Symbol> symbols) {
		this.width = width;
		this.height = height;
		this.name = name;
		this.code = code;
		this.created = created;
		this.modified = modified;
		this.symbols = symbols;
	}

	public WorldInfo(int width, int height, String name, String code, List<Symbol> symbols) {
		this(width, height, name, code, timestamp(), timestamp(), symbols);
	}

	private static long timestamp() {
		return Instant.now().getEpochSecond();
	}

	public boolean isPositionValid(int x, int y) {
		return x >= 0 && y >= 0 && x < width && y < height;
	}

	public String getMapName() {
		return name;
	}

	public String getCodeName() {
		return code;
	}

	public String getCreateTime(DateTimeFormatter formatter) {
		return LocalDateTime.ofEpochSecond(created, 0, ZoneOffset.UTC).format(formatter);
	}

	public String getModifyTime(DateTimeFormatter formatter) {
		return LocalDateTime.ofEpochSecond(modified, 0, ZoneOffset.UTC).format(formatter);
	}

	public List<Symbol> getSymbols() {
		return symbols;
	}

	public void toNbt(CompoundTag nbt) {
		nbt.putInt("width", width);
		nbt.putInt("height", height);
		nbt.putString("name", name);
		nbt.putString("code", code);
		nbt.putLong("created", created);
		nbt.putLong("modified", timestamp());
		nbt.putLong("version", 1);

		ListTag<ByteTag> symbolsTagList = new ListTag<>(ByteTag.class);

		for (Symbol symbol : symbols) {
			symbolsTagList.add(new ByteTag ((byte) symbol.ordinal()));
		}

		nbt.put("symbols", symbolsTagList);
	}

	public static WorldInfo load(CompoundTag nbt) {
		int width = nbt.getInt("width");
		int height = nbt.getInt("height");
		String name = nbt.getString("name");
		String code = nbt.getString("code");
		long created = nbt.getLong("created");
		long modified = nbt.getLong("modified");
		long version = nbt.getLong("version");

		if (version != 1) {
			throw new RuntimeException("Unknown save version!");
		}

		ListTag<ByteTag> symbolsTagList = nbt.getListTag("symbols").asByteTagList();
		List<Symbol> symbols = new ArrayList<>(symbolsTagList.size());

		for (ByteTag symbolTag : symbolsTagList) {
			symbols.add(Symbol.fromIndex(symbolTag.asByte()));
		}

		return new WorldInfo(width, height, name, code, created, modified, Collections.unmodifiableList(symbols));
	}

}
