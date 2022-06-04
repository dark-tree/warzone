package net.darktree.game.buildings;

import net.darktree.core.client.render.image.Sprite;
import net.darktree.core.util.Type;

import java.util.ArrayList;
import java.util.List;

public class BuildingManager {

	private static List<BuildingEntry> entries = new ArrayList<>();

	public static void register(Type<Building> type, int cost, String name, String description, Sprite icon) {
		entries.add(new BuildingEntry(type, cost, name, description, icon));
	}

	public static List<BuildingEntry> getEntries() {
		return entries;
	}

}
