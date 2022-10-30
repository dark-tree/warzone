package net.darktree.core.world.tiles;

import net.darktree.core.Registries;
import net.darktree.core.client.Sprites;
import net.darktree.core.util.BuildingType;
import net.darktree.core.world.entity.Entity;
import net.darktree.core.world.entity.UnitEntity;
import net.darktree.core.world.entity.building.CapitolBuilding;
import net.darktree.core.world.entity.building.FactoryBuilding;
import net.darktree.core.world.entity.building.MineBuilding;
import net.darktree.core.world.tile.Tile;

public class Tiles {
	public static Tile EMPTY = Registries.TILES.register("empty", new EmptyTile());
	public static Tile MATERIAL = Registries.TILES.register("material_ore", new MaterialOreTile());
	public static Tile WATER = Registries.TILES.register("water", new WaterTile());

	public static Entity.Type UNIT = Registries.ENTITIES.register("unit", new Entity.Type(UnitEntity::new));

	public static BuildingType CAPITOL = Registries.ENTITIES.register("capitol", new BuildingType(CapitolBuilding::new, 0, 2, 2, Sprites.BUILDING_CAPITOL));
	public static BuildingType FACTORY = Registries.ENTITIES.register("factory", new BuildingType(FactoryBuilding::new, 10, 2, 2, Sprites.BUILDING_FACTORY));
	public static BuildingType MINE = Registries.ENTITIES.register("mine", new BuildingType(MineBuilding::new, 0, 1, 1, Sprites.BUILDING_MINE));

}
