package net.darktree.game.tiles;

import net.darktree.game.buildings.Building;
import net.darktree.game.entites.TestEntity;
import net.darktree.lt2d.Registries;
import net.darktree.lt2d.util.Type;
import net.darktree.lt2d.world.Tile;
import net.darktree.lt2d.world.entities.Entity;

public class Tiles {
	public static Tile EMPTY = Registries.TILES.register("empty", new EmptyTile());
	public static Tile MATERIAL = Registries.TILES.register("material_ore", new MaterialOreTile());
	public static Tile MATERIAL_MINE = Registries.TILES.register("material_mine", new MaterialMineTile());
	public static Tile WATER = Registries.TILES.register("water", new WaterTile());

	public static Type<Entity> TEST = Registries.ENTITIES.register("circle", new Type<>(TestEntity::new));

	public static Type<Building> BUILD = Registries.BUILDINGS.register("build", new Type<>(Building::new));

	static {
		Registries.TILES.freeze();
	}
}
