package net.darktree.warzone.world.terrain;

import com.google.common.collect.ImmutableMap;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.util.Util;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.pattern.Patterns;
import net.darktree.warzone.world.tile.TilePos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BorderFinder extends AbstractFieldFinder {

	private final ImmutableMap<Symbol, List<TilePos>> tiles = Util.enumMapOf(Symbol.class, symbol -> new ArrayList<>());

	public BorderFinder(World world) {
		super(Patterns.NEIGHBOURS, world);

		compute();
	}

	/**
	 * Check if a tile at the given pos is a border tile
	 */
	public boolean isBorderTile(int x, int y) {
		return field[x][y] != 0;
	}

	/**
	 * Get a list of the positions of all the border tiles on the map
	 */
	public List<TilePos> getBorderTiles(Symbol symbol) {
		return Collections.unmodifiableList(tiles.get(symbol));
	}

	private void compute() {
		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				Symbol owner = getOwner(x, y);
				boolean border = isBorderTile(x, y, owner);

				if (border) {
					field[x][y] = 1;
					tiles.get(owner).add(new TilePos(x, y));
				}
			}
		}
	}

	private boolean isBorderTile(int x, int y, Symbol owner) {
		for (TilePos offset : offsets) {
			int tx = x + offset.x, ty = y + offset.y;

			if (isPosValid(tx, ty) && getOwner(tx, ty) != owner) {
				return true;
			}
		}

		return false;
	}

}
