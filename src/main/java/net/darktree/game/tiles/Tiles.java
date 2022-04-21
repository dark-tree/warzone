package net.darktree.game.tiles;

import net.darktree.game.entites.TestEntity;
import net.darktree.lt2d.Registries;
import net.darktree.lt2d.world.Tile;
import net.darktree.lt2d.world.entities.Entity;

public class Tiles {
	public static Tile EMPTY = Registries.TILES.register("empty", new EmptyTile());
	public static Tile MATERIAL = Registries.TILES.register("material_ore", new MaterialOreTile());
	public static Tile MATERIAL_MINE = Registries.TILES.register("material_mine", new MaterialMineTile());
	public static Tile WATER = Registries.TILES.register("water", new WaterTile());

	public static Entity.Type<?> TEST = Registries.ENTITIES.register("circle", new Entity.Type<>(TestEntity.class));

	static {
		Registries.TILES.freeze();
	}
}
