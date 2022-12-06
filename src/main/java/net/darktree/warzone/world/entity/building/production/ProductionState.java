package net.darktree.warzone.world.entity.building.production;

import net.darktree.warzone.country.Country;

import java.util.ArrayList;
import java.util.List;

public class ProductionState {

	private final int capacity;
	private final List<Recipe> recipes = new ArrayList<>();

	public ProductionState(int capacity) {
		this.capacity = capacity;
	}

	public void register(Recipe.Type recipe) {
		recipes.add(recipe.create());
	}

	public List<Recipe> getRecipes() {
		return recipes;
	}

	private int getQuantity() {
		return recipes.stream().mapToInt(Recipe::getQuantity).sum();
	}

	public boolean canProduce() {
		return capacity == -1 || getQuantity() < capacity;
	}

	public String getCapacityString() {
		return capacity == -1 ? "UNLIMITED" : getQuantity() + "/" + capacity;
	}

	public void apply(Country country) {
		recipes.forEach(recipe -> recipe.apply(country));
	}

}
