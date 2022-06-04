package net.darktree.game.buildings;

import net.darktree.core.client.render.image.Sprite;
import net.darktree.core.util.Type;

public class BuildingEntry {

	public final Type<Building> type;
	public final int cost;
	public final String name;
	public final String description;
	public final Sprite icon;

	public BuildingEntry(Type<Building> type, int cost, String name, String description, Sprite icon) {
		this.type = type;
		this.cost = cost;
		this.name = name;
		this.description = description;
		this.icon = icon;
	}

}
