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
	public static final Tile EMPTY = Registries.TILES.register("empty", new EmptyTile());
	public static final Tile MATERIAL_ORE = Registries.TILES.register("material_ore", new MaterialOreTile());
	public static final Tile WATER = Registries.TILES.register("water", new WaterTile());

	// TODO: move to a separate class
	public static final Entity.Type UNIT = Registries.ENTITIES.register("unit", new Entity.Type(UnitEntity::new));

	// TODO: move to a separate class
	public static final Building.Type CAPITOL = Registries.ENTITIES.register("capitol", new Building.Type(CapitolBuilding::new, 0, 2, 2, Sprites.BUILDING_CAPITOL));
	public static final Building.Type FACTORY = Registries.ENTITIES.register("factory", new Building.Type(FactoryBuilding::new, 10, 2, 2, Sprites.BUILDING_FACTORY));
	public static final Building.Type MINE = Registries.ENTITIES.register("mine", new Building.Type(MineBuilding::new, 0, 1, 1, Sprites.BUILDING_MINE));
}
