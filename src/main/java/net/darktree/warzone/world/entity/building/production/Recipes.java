package net.darktree.warzone.world.entity.building.production;

import net.darktree.warzone.Registries;
import net.darktree.warzone.country.Resources;

public class Recipes {
	public static Recipe.Type AMMO = Registries.RECIPES.register("ammo", RecipeBuilder.create(Resources.AMMO).in(Resources.MATERIALS, 1).build());
	public static Recipe.Type ARMOR = Registries.RECIPES.register("armor", RecipeBuilder.create(Resources.ARMOR).in(Resources.MATERIALS, 1).build());
}
