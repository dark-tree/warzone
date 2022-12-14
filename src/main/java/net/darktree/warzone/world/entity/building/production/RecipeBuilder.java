package net.darktree.warzone.world.entity.building.production;

import net.darktree.warzone.country.Resource;

import java.util.ArrayList;

public class RecipeBuilder {

	private final ArrayList<Resource.Quantified> input = new ArrayList<>();
	private final ArrayList<Resource.Quantified> output = new ArrayList<>();

	public static RecipeBuilder of(Resource resource) {
		RecipeBuilder builder = new RecipeBuilder();
		builder.output.add(resource.quantify(1));
		return builder;
	}

	public RecipeBuilder takes(Resource resource, int quantity) {
		input.add(resource.quantify(quantity));
		return this;
	}

	public Recipe.Type build() {
		return new Recipe.Type(input.toArray(Resource.Quantified[]::new), output.toArray(Resource.Quantified[]::new));
	}

}
