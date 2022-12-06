package net.darktree.warzone.world.entity.building.production;

import com.google.common.base.Joiner;
import net.darktree.warzone.Registries;
import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Resource;
import net.darktree.warzone.util.ElementType;
import net.darktree.warzone.util.Registry;

import java.util.function.Consumer;

public class Recipe {

	private final Recipe.Type type;
	private int quantity;

	public Recipe(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public int getQuantity() {
		return quantity;
	}

	public boolean canProduce(Country country) {
		for (Resource.Quantified resource : type.input) {
			if (!country.hasResource(resource)) return false;
		}

		return true;
	}

	public void redo(Country country) {
		type.forEachInput(country::removeResource);
		quantity ++;
	}

	public void undo(Country country) {
		type.forEachInput(country::addResource);
		quantity --;
	}

	public void apply(Country country) {
		while (quantity > 0) {
			type.forEachOutput(country::addResource);
			quantity --;
		}
	}

	public final static class Type extends ElementType<Recipe.Type> {

		private final Resource.Quantified[] input;
		private final Resource.Quantified[] output;
		private final String cost;

		public Type(Resource.Quantified[] input, Resource.Quantified[] output) {
			this.input = input;
			this.output = output;
			this.cost = Joiner.on(" ").join(input);
		}

		@Override
		public Registry<Type> getRegistry() {
			return Registries.RECIPES;
		}

		public String getCostString() {
			return cost;
		}

		public String getNameString() {
			return output[0].toString();
		}

		public void forEachInput(Consumer<Resource.Quantified> consumer) {
			for (Resource.Quantified resource : input) consumer.accept(resource);
		}

		public void forEachOutput(Consumer<Resource.Quantified> consumer) {
			for (Resource.Quantified resource : output) consumer.accept(resource);
		}

		public Recipe create() {
			return new Recipe(this);
		}

	}

}
