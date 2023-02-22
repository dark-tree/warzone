package net.darktree.warzone.world.entity;

import net.darktree.warzone.Registries;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.world.entity.building.*;

public class Entities {
	public static final Entity.Type UNIT = Registries.ENTITIES.register("unit", new Entity.Type(UnitEntity::new));

	// buildings
	public static final Building.Type CAPITOL = Registries.ENTITIES.register("capitol", Building.Type.create(CapitolBuilding::new, 0, 2, 2, Sprites.BUILDING_CAPITOL).build());
	public static final Building.Type FACTORY = Registries.ENTITIES.register("factory", Building.Type.create(FactoryBuilding::new, 10, 2, 2, Sprites.BUILDING_FACTORY).build());
	public static final Building.Type MINE = Registries.ENTITIES.register("mine", Building.Type.create(MineBuilding::new, 0, 1, 1, Sprites.BUILDING_MINE).build());
	public static final Building.Type WAREHOUSE = Registries.ENTITIES.register("warehouse", Building.Type.create(WarehouseBuilding::new, 10, 3, 2, Sprites.BUILDING_WAREHOUSE_LONG).withIcon(Sprites.BUILDING_WAREHOUSE).build());
	public static final Building.Type PARLIAMENT = Registries.ENTITIES.register("parliament", Building.Type.create(ParliamentBuilding::new, 10, 2, 2, Sprites.BUILDING_PARLIAMENT).build());
	public static final Building.Type FENCE = Registries.ENTITIES.register("fence", Building.Type.create(FenceStructure::new, 1, 1, 1, Sprites.STRUCTURE_FENCE).rotatable().build());
	public static final Building.Type WALL = Registries.ENTITIES.register("wall", Building.Type.create(WallStructure::new, 3, 1, 1, Sprites.STRUCTURE_WALL).rotatable().build());
}
