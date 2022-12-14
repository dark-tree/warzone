package net.darktree.warzone.world.entity.building.production;

import net.darktree.warzone.Registries;
import net.darktree.warzone.country.Resources;

public class Recipes {
	public static Recipe.Type AMMO = Registries.RECIPES.register("ammo", RecipeBuilder.of(Resources.AMMO).takes(Resources.MATERIALS, 1).build());
	public static Recipe.Type ARMOR = Registries.RECIPES.register("armor", RecipeBuilder.of(Resources.ARMOR).takes(Resources.MATERIALS, 1).build());
}
