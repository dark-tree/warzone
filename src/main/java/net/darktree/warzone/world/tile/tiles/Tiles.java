package net.darktree.warzone.world.tile.tiles;

import net.darktree.warzone.Registries;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.entity.UnitEntity;
import net.darktree.warzone.world.entity.building.Building;
import net.darktree.warzone.world.entity.building.CapitolBuilding;
import net.darktree.warzone.world.entity.building.FactoryBuilding;
import net.darktree.warzone.world.entity.building.MineBuilding;
import net.darktree.warzone.world.tile.Tile;

public class Tiles {
	public static Tile EMPTY = Registries.TILES.register("empty", new EmptyTile());
	public static Tile MATERIAL_ORE = Registries.TILES.register("material_ore", new MaterialOreTile());
	public static Tile WATER = Registries.TILES.register("water", new WaterTile());

	public static Entity.Type UNIT = Registries.ENTITIES.register("unit", new Entity.Type(UnitEntity::new));

	public static Building.Type CAPITOL = Registries.ENTITIES.register("capitol", new Building.Type(CapitolBuilding::new, 0, 2, 2, Sprites.BUILDING_CAPITOL));
	public static Building.Type FACTORY = Registries.ENTITIES.register("factory", new Building.Type(FactoryBuilding::new, 10, 2, 2, Sprites.BUILDING_FACTORY));
	public static Building.Type MINE = Registries.ENTITIES.register("mine", new Building.Type(MineBuilding::new, 0, 1, 1, Sprites.BUILDING_MINE));

}
