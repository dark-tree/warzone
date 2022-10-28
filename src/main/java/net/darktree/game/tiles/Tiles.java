package net.darktree.game.tiles;

import net.darktree.core.Registries;
import net.darktree.core.client.Sprites;
import net.darktree.core.util.BuildingType;
import net.darktree.core.world.entity.Entity;
import net.darktree.core.world.tile.Tile;
import net.darktree.game.buildings.CapitolBuilding;
import net.darktree.game.buildings.FactoryBuilding;
import net.darktree.game.buildings.MineBuilding;
import net.darktree.game.entities.UnitEntity;

public class Tiles {
	public static Tile EMPTY = Registries.TILES.register("empty", new EmptyTile());
	public static Tile MATERIAL = Registries.TILES.register("material_ore", new MaterialOreTile());
	public static Tile WATER = Registries.TILES.register("water", new WaterTile());

	public static Entity.Type UNIT = Registries.ENTITIES.register("unit", new Entity.Type(UnitEntity::new));

	public static BuildingType CAPITOL = Registries.ENTITIES.register("capitol", new BuildingType(CapitolBuilding::new, 0, 2, 2, Sprites.BUILDING_CAPITOL));
	public static BuildingType FACTORY = Registries.ENTITIES.register("factory", new BuildingType(FactoryBuilding::new, 10, 2, 2, Sprites.BUILDING_FACTORY));
	public static BuildingType MINE = Registries.ENTITIES.register("mine", new BuildingType(MineBuilding::new, 0, 1, 1, Sprites.ATLAS.getSprite("tile/material_mine.png"))); //TODO

}
