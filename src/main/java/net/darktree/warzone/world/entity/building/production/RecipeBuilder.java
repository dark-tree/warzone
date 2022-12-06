package net.darktree.warzone.world.entity.building.production;

import net.darktree.warzone.country.Resource;

import java.util.ArrayList;

public class RecipeBuilder {

	private final ArrayList<Resource.Quantified> input = new ArrayList<>();
	private final ArrayList<Resource.Quantified> output = new ArrayList<>();

	public static RecipeBuilder create() {
		return new RecipeBuilder();
	}

	public static RecipeBuilder create(Resource resource) {
		return create().out(resource);
	}

	public RecipeBuilder in(Resource.Quantified resource) {
		input.add(resource);
		return this;
	}

	public RecipeBuilder in(Resource resource, int quantity) {
		return in(resource.quantify(quantity));
	}

	public RecipeBuilder in(Resource resource) {
		return in(resource, 1);
	}

	public RecipeBuilder out(Resource.Quantified resource) {
		output.add(resource);
		return this;
	}

	public RecipeBuilder out(Resource resource, int quantity) {
		return out(resource.quantify(quantity));
	}

	public RecipeBuilder out(Resource resource) {
		return out(resource, 1);
	}

	public Recipe.Type build() {
		return new Recipe.Type(input.toArray(Resource.Quantified[]::new), output.toArray(Resource.Quantified[]::new));
	}

}
