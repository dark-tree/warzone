package net.darktree.game.tiles;

import net.darktree.game.entites.TestEntity;
import net.darktree.lt2d.Registries;
import net.darktree.lt2d.world.entities.Entity;
import net.darktree.lt2d.world.Tile;

public class Tiles {
	public static Tile EMPTY = Registries.TILES.register("empty", new EmptyTile());
	public static Tile CROSS = Registries.TILES.register("cross", new CrossTile());
	public static Tile CIRCLE = Registries.TILES.register("circle", new CircleTile());

	public static Entity.Type<?> TEST = Registries.ENTITIES.register("circle", new Entity.Type<>(TestEntity.class));

	static {
		Registries.TILES.freeze();
	}
}
