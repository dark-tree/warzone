package net.darktree.warzone.world.entity.building.production;

import net.darktree.warzone.country.Country;
import net.darktree.warzone.util.NbtSerializable;
import net.darktree.warzone.world.entity.building.ProducingBuilding;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.IntTag;
import net.querz.nbt.tag.ListTag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProductionState implements NbtSerializable {

	private final int capacity;
	private final ProducingBuilding parent;
	private final List<Recipe> recipes = new ArrayList<>();

	public ProductionState(int capacity, ProducingBuilding parent) {
		this.capacity = capacity;
		this.parent = parent;
	}

	@Override
	public void toNbt(@NotNull CompoundTag tag) {
		ListTag<IntTag> values = new ListTag<>(IntTag.class);

		for (Recipe recipe : recipes) {
			values.add(new IntTag(recipe.getQuantity()));
		}

		tag.put("recipes", values);
	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {
		ListTag<IntTag> values = tag.getListTag("recipes").asTypedList(IntTag.class);
		final int size = Math.min(recipes.size(), values.size());
		final Country owner = parent.getOwner().orElseThrow();

		for (Recipe recipe : recipes) {
			recipe.reset(owner);
		}

		for (int i = 0; i < size; i ++) {
			int quantity = values.get(i).asInt();

			while (quantity --> 0) {
				if (!canProduce()) {
					break;
				}

				recipes.get(i).redo(owner);
			}
		}
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

	public void sync() {
		parent.sync();
	}

}
