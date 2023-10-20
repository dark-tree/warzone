package net.darktree.warzone.world.entity;

import net.darktree.warzone.Registries;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.screen.interactor.BridgeBuildInteractor;
import net.darktree.warzone.screen.interactor.RotatableBuildInteractor;
import net.darktree.warzone.world.entity.building.*;

public class Entities {
	public static final Entity.Type UNIT = Registries.ENTITIES.register("unit", new Entity.Type(UnitEntity::new, Symbol.CROSS.getSprite()));

	// buildings
	public static final Building.Type CAPITOL = Registries.ENTITIES.register("capitol", Building.define(CapitolBuilding::new, Sprites.BUILDING_CAPITOL).size(2, 2).build());
	public static final Building.Type FACTORY = Registries.ENTITIES.register("factory", Building.define(FactoryBuilding::new, Sprites.BUILDING_FACTORY).cost(10).size(2, 2).build());
	public static final Building.Type MINE = Registries.ENTITIES.register("mine", Building.define(MineBuilding::new, Sprites.BUILDING_MINE).build());
	public static final Building.Type WAREHOUSE = Registries.ENTITIES.register("warehouse", Building.define(WarehouseBuilding::new, Sprites.BUILDING_WAREHOUSE_LONG).cost(10).size(3, 2).icon(Sprites.BUILDING_WAREHOUSE).build());
	public static final Building.Type PARLIAMENT = Registries.ENTITIES.register("parliament", Building.define(ParliamentBuilding::new, Sprites.BUILDING_PARLIAMENT).cost(10).size(2, 2).build());
	public static final Building.Type FENCE = Registries.ENTITIES.register("fence", Building.define(FenceStructure::new, Sprites.STRUCTURE_FENCE).cost(1).interactor(RotatableBuildInteractor::new).build());
	public static final Building.Type WALL = Registries.ENTITIES.register("wall", Building.define(WallStructure::new, Sprites.STRUCTURE_WALL).cost(3).interactor(RotatableBuildInteractor::new).build());
	public static final Building.Type BRIDGE = Registries.ENTITIES.register("bridge", Building.define(BridgeStructure::new, Sprites.STRUCTURE_BRIDGE).cost(2).interactor(BridgeBuildInteractor::new).build());
}
