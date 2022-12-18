package net.darktree.warzone.world.entity.building;

import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.building.production.Recipes;
import net.darktree.warzone.world.tile.tiles.Tiles;

public class FactoryBuilding extends ProducingBuilding {

	public FactoryBuilding(World world, int x, int y) {
		super(world, x, y, Tiles.FACTORY, 2);

		production.register(Recipes.AMMO);
		production.register(Recipes.ARMOR);
	}

}
