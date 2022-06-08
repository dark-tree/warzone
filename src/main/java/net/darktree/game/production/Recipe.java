package net.darktree.game.production;

import net.darktree.core.world.World;
import net.darktree.game.country.Symbol;

public abstract class Recipe {

	private final String name;
	private final String cost;

	public int quantity = 0;

	public Recipe(String name, String cost) {
		this.name = name;
		this.cost = cost;
	}

	public String getCostString() {
		return cost;
	}

	public String getNameString() {
		return name;
	}

	public int getQuantity() {
		return quantity;
	}

	/**
	 * Check if the required resources is available
	 */
	public boolean canProduce(ProductionState state, World world, Symbol symbol) {
		return state.canProduce();
	}

	/**
	 * Take required resources
	 */
	public void redo(ProductionState state, World world, Symbol symbol) {
		quantity ++;
	}

	/**
	 * Returned required resources
	 */
	public void undo(ProductionState state, World world, Symbol symbol) {
		quantity --;
	}

	/**
	 * Add the output resource into the target
	 */
	public void apply(ProductionState state, World world, Symbol symbol) {
		quantity = 0;
	}

}
