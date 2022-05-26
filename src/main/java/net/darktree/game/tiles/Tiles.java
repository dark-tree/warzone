package net.darktree.game.tiles;

import net.darktree.core.Registries;
import net.darktree.core.util.Type;
import net.darktree.core.world.entity.Entity;
import net.darktree.core.world.tile.Tile;
import net.darktree.game.buildings.Building;
import net.darktree.game.buildings.CapitolBuilding;
import net.darktree.game.entities.UnitEntity;

public class Tiles {
	public static Tile EMPTY = Registries.TILES.register("empty", new EmptyTile());
	public static Tile MATERIAL = Registries.TILES.register("material_ore", new MaterialOreTile());
	public static Tile MATERIAL_MINE = Registries.TILES.register("material_mine", new MaterialMineTile());
	public static Tile WATER = Registries.TILES.register("water", new WaterTile());
	public static Tile STRUCTURE = Registries.TILES.register("structure", new StructureTile());

	public static Type<Entity> TEST = Registries.ENTITIES.register("unit", new Type<>(UnitEntity::new));

	public static Type<Building> BUILD = Registries.BUILDINGS.register("capitol", new Type<>(CapitolBuilding::new));
}
