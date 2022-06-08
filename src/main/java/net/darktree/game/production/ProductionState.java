package net.darktree.game.production;

import net.darktree.core.world.World;
import net.darktree.game.country.Symbol;

import java.util.ArrayList;
import java.util.List;

public class ProductionState {

	private final int capacity;
	private final List<Recipe> recipes = new ArrayList<>();

	public ProductionState(int capacity) {
		this.capacity = capacity;
	}

	public void register(Recipe recipe) {
		recipes.add(recipe);
	}

	public List<Recipe> getRecipes() {
		return recipes;
	}

	private int getQuantity() {
		return recipes.stream().map(Recipe::getQuantity).reduce(0, Integer::sum);
	}

	public boolean canProduce() {
		return capacity == -1 || getQuantity() < capacity;
	}

	public String getCapacityString() {
		return capacity == -1 ? "UNLIMITED" : getQuantity() + "/" + capacity;
	}

	public void apply(World world, Symbol symbol) {
		recipes.forEach(recipe -> {
			if (recipe.quantity > 0) {
				recipe.apply(this, world, symbol);
			}
		});
	}

}
