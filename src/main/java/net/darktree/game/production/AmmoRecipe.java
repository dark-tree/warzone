package net.darktree.game.production;

import net.darktree.core.world.World;
import net.darktree.game.country.Symbol;

public class AmmoRecipe extends Recipe {

	public AmmoRecipe() {
		super("AMMUNITION", "1m");
	}

	/**
	 * Check if the required resources is available
	 */
	public boolean verify(ProductionState state, World world, Symbol symbol) {
		return super.verify(state, world, symbol) && world.getCountry(symbol).getTotalMaterials() >= 1;
	}

	/**
	 * Take required resources
	 */
	public void redo(ProductionState state, World world, Symbol symbol) {
		quantity ++;
		world.getCountry(symbol).addMaterials(-1);
	}

	/**
	 * Returned required resources
	 */
	public void undo(ProductionState state, World world, Symbol symbol) {
		quantity --;
		world.getCountry(symbol).addMaterials(+1);
	}

	/**
	 * Add the output resource into the target
	 */
	public void apply(ProductionState state, World world, Symbol symbol) {
		quantity = 0;
		world.getCountry(symbol).ammo ++;
	}

}
