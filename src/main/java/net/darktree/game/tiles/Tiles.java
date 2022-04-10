package net.darktree.game.tiles;

import net.darktree.lt2d.Registries;
import net.darktree.lt2d.world.Tile;

public class Tiles {
	public static Tile EMPTY = Registries.TILES.register("empty", new EmptyTile());
}
