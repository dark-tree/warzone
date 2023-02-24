package net.darktree.warzone.world.action;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.country.upgrade.Upgrades;
import net.darktree.warzone.util.Direction;
import net.darktree.warzone.util.math.Vec2i;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.Entities;
import net.darktree.warzone.world.entity.building.BridgeStructure;
import net.darktree.warzone.world.tile.Surface;
import net.darktree.warzone.world.tile.TilePos;
import net.darktree.warzone.world.tile.TileState;

import java.util.ArrayList;
import java.util.List;

public class BridgePlacer {

	private final World world;
	private final Symbol symbol;
	private final Direction facing;
	private final List<TilePos> tiles;
	private TilePos a, b;

 	private BridgePlacer(World world, Symbol symbol, Direction facing) {
		this.world = world;
		this.symbol = symbol;
		this.facing = facing;
		this.tiles = new ArrayList<>();
	}

	public int getLength() {
		 return tiles.size();
	}

	public int getCost() {
		 return Entities.BRIDGE.value * getLength();
	}

	public List<TilePos> getTiles() {
		return tiles;
	}

	/**
	 * Configure all parts of a bridge as designed by this placer
	 */
	public void configure(List<BridgeStructure> parts) {
		 int last = parts.size() - 1;

		 for (int i = 0; i < parts.size(); i ++) {
			 parts.get(i).configure(a, b, i == 0 || i == last, facing);
		 }
	}

	/**
	 * Check if the player can build this bridge
	 */
	public boolean isFullyValid() {
		return isShoreValid() && isLengthValid() && isCostValid();
	}

	/**
	 * Check if the player can build a bridge on those shores
	 */
	public boolean isShoreValid() {
		return a != null && b != null && (world.canControl(a.x, a.y, symbol) || world.canControl(b.x, b.y, symbol));
	}

	/**
	 * Check if the bridge length is valid for this player
	 */
	public boolean isLengthValid() {
		return getLength() <= world.getCountry(symbol).upgrades.get(Upgrades.BRIDGE);
	}

	/**
	 * Check if the bridge is not too expensive for the player
	 */
	public boolean isCostValid() {
		return getCost() <= world.getCountry(symbol).getTotalMaterials();
	}

	/**
	 * Projects the bridge structure, returns object that can be used to validate, render
	 * and build the requested bridge, or null if no bridge can possibly exist at the given position
	 */
	public static BridgePlacer create(World world, Symbol symbol, int x, int y, Direction facing) {
		BridgePlacer placer = new BridgePlacer(world, symbol, facing);

		if (world.isPositionValid(x, y) && isTileValid(world, x, y)) {
			placer.tiles.add(new TilePos(x, y));

			placer.a = projectBridgeSide(world, x, y, facing.getOffset(), placer.tiles);
			placer.b = projectBridgeSide(world, x, y, facing.opposite().getOffset(), placer.tiles);
			return placer;
		}

		return null;
	}

	private static TilePos projectBridgeSide(World world, int x, int y, Vec2i offset, List<TilePos> tiles) {
		int cx = x, cy = y;

		while (true) {
			cx += offset.x;
			cy += offset.y;

			if (!world.isPositionValid(cx, cy)) {
				return null;
			}

			if (!isTileValid(world, cx, cy)) {
				return new TilePos(cx, cy);
			}

			tiles.add(new TilePos(cx, cy));
		}
	}

	private static boolean isTileValid(World world, int x, int y) {
		final TileState state = world.getTileState(x, y);
		return state.getEntity() == null && state.getTile().getSurface() == Surface.WATER;
	}

}
