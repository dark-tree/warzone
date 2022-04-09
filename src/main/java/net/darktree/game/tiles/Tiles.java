package net.darktree.game.tiles;

import net.darktree.game.Registries;
import net.darktree.game.Tile;

public class Tiles {
	public static Tile EMPTY = Registries.TILES.register("empty", new EmptyTile());
}
