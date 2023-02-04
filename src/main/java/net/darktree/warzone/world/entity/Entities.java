package net.darktree.warzone.world.entity;

import net.darktree.warzone.Registries;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.world.entity.building.*;

public class Entities {
	public static final Entity.Type UNIT = Registries.ENTITIES.register("unit", new Entity.Type(UnitEntity::new));

	// TODO: move to a separate class(?)
	public static final Building.Type CAPITOL = Registries.ENTITIES.register("capitol", new Building.Type(CapitolBuilding::new, 0, 2, 2, Sprites.BUILDING_CAPITOL));
	public static final Building.Type FACTORY = Registries.ENTITIES.register("factory", new Building.Type(FactoryBuilding::new, 10, 2, 2, Sprites.BUILDING_FACTORY));
	public static final Building.Type MINE = Registries.ENTITIES.register("mine", new Building.Type(MineBuilding::new, 0, 1, 1, Sprites.BUILDING_MINE));
	public static final Building.Type WAREHOUSE = Registries.ENTITIES.register("warehouse", new Building.Type(WarehouseBuilding::new, 10, 3, 2, Sprites.BUILDING_WAREHOUSE, Sprites.BUILDING_WAREHOUSE_LONG));
	public static final Building.Type PARLIAMENT = Registries.ENTITIES.register("parliament", new Building.Type(ParliamentBuilding::new, 10, 2, 2, Sprites.BUILDING_PARLIAMENT));
}
