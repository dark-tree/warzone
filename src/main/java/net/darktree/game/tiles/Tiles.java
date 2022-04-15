package net.darktree.game.tiles;

import net.darktree.lt2d.Registries;
import net.darktree.lt2d.world.Tile;

public class Tiles {
	public static Tile EMPTY = Registries.TILES.register("empty", new EmptyTile());
	public static Tile CROSS = Registries.TILES.register("cross", new CrossTile());
	public static Tile CIRCLE = Registries.TILES.register("circle", new CircleTile());

	static {
		Registries.TILES.freeze();
	}
}
