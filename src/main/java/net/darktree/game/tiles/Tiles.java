package net.darktree.game.tiles;

import net.darktree.game.Registries;
import net.darktree.game.Tile;

public class Tiles {
	public static Tile.Type EMPTY = Registries.TILES.register("empty", Tile.Type.of(EmptyTile::new, EmptyTile.STATE, EmptyTile.DELETED));
}
