package net.darktree.warzone;

import net.darktree.warzone.country.Resource;
import net.darktree.warzone.country.upgrade.Upgrade;
import net.darktree.warzone.network.Packet;
import net.darktree.warzone.util.Registry;
import net.darktree.warzone.world.action.ledger.Action;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.entity.building.production.Recipe;
import net.darktree.warzone.world.tile.Tile;

public class Registries {
	public static final Registry<Tile> TILES = new Registry<>();
	public static final Registry<Entity.Type> ENTITIES = new Registry<>();
	public static final Registry<Packet.Type> PACKETS = new Registry<>();
	public static final Registry<Action.Type> ACTIONS = new Registry<>();
	public static final Registry<Recipe.Type> RECIPES = new Registry<>();
	public static final Registry<Resource> RESOURCES = new Registry<>();
	public static final Registry<Upgrade<?>> UPGRADES = new Registry<>();
}
