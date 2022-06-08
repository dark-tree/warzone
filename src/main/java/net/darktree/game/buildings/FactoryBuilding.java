package net.darktree.game.buildings;

import net.darktree.core.util.BuildingType;
import net.darktree.core.world.World;
import net.darktree.game.production.AmmoRecipe;
import net.darktree.game.production.ArmorRecipe;

public class FactoryBuilding extends ProducingBuilding {

	public FactoryBuilding(World world, int x, int y, BuildingType type) {
		super(world, x, y, type, 2);

		production.register(new AmmoRecipe());
		production.register(new ArmorRecipe());
	}

}
