package net.darktree.core.world.entity.building;

import net.darktree.core.world.World;
import net.darktree.core.world.tiles.Tiles;
import net.darktree.game.production.AmmoRecipe;
import net.darktree.game.production.ArmorRecipe;

public class FactoryBuilding extends ProducingBuilding {

	public FactoryBuilding(World world, int x, int y) {
		super(world, x, y, Tiles.FACTORY, 2);

		production.register(new AmmoRecipe());
		production.register(new ArmorRecipe());
	}

}
