package net.darktree.warzone.world.tile.tiles;

import net.darktree.warzone.Registries;
import net.darktree.warzone.world.tile.Tile;

public class Tiles {
	public static final Tile EMPTY = Registries.TILES.register("empty", new EmptyTile());
	public static final Tile MATERIAL_ORE = Registries.TILES.register("material_ore", new MaterialOreTile());
	public static final Tile WATER = Registries.TILES.register("water", new WaterTile());
}
