package net.darktree.game.buildings;

import net.darktree.core.util.BuildingType;

import java.util.ArrayList;
import java.util.List;

public class BuildingConfigRegistry {

	private static final List<Config> entries = new ArrayList<>();

	public static void register(BuildingType type, String name, String description) {
		entries.add(new Config(type, name, description));
	}

	public static List<Config> getEntries() {
		return entries;
	}

	public static class Config {
		public final BuildingType type;
		public final String name;
		public final String description;

		public Config(BuildingType type, String name, String description) {
			this.type = type;
			this.name = name;
			this.description = description;
		}
	}

}
