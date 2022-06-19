package net.darktree.game.tiles;

import net.darktree.core.Registries;
import net.darktree.core.client.Sprites;
import net.darktree.core.util.BuildingType;
import net.darktree.core.util.Type;
import net.darktree.core.world.Pattern;
import net.darktree.core.world.entity.Entity;
import net.darktree.core.world.tile.Tile;
import net.darktree.game.buildings.CapitolBuilding;
import net.darktree.game.buildings.FactoryBuilding;
import net.darktree.game.entities.UnitEntity;

public class Tiles {
	public static Tile EMPTY = Registries.TILES.register("empty", new EmptyTile());
	public static Tile MATERIAL = Registries.TILES.register("material_ore", new MaterialOreTile());
	public static Tile MATERIAL_MINE = Registries.TILES.register("material_mine", new MaterialMineTile());
	public static Tile WATER = Registries.TILES.register("water", new WaterTile());
	public static Tile STRUCTURE = Registries.TILES.register("structure", new StructureTile());

	public static Type<Entity> UNIT = Registries.ENTITIES.register("unit", new Type<>(UnitEntity::new));

	public static BuildingType CAPITOL = Registries.BUILDINGS.register("capitol", new BuildingType(CapitolBuilding::new, 0, Pattern.SQUARE, Sprites.BUILDING_CAPITOL));
	public static BuildingType FACTORY = Registries.BUILDINGS.register("factory", new BuildingType(FactoryBuilding::new, 10, Pattern.SQUARE, Sprites.BUILDING_FACTORY));

}
