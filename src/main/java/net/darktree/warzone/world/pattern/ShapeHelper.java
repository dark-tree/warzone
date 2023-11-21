package net.darktree.warzone.world.pattern;

import net.darktree.warzone.util.Direction;
import net.darktree.warzone.util.math.MathHelper;
import net.darktree.warzone.world.Warp;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.tile.TilePos;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class ShapeHelper {

	public static boolean isValid(WorldSnapshot world, TargetPredicate target, MidpointPredicate midpoint, boolean large, int fx, int fy, int tx, int ty) {
		if (!world.isPositionValid(fx, fy) || !world.isPositionValid(tx, ty)) {
			return false;
		}

		final int md = MathHelper.getManhattanDistance(fx, fy, tx, ty);
		final int cd = MathHelper.getChebyshevDistance(fx, fy, tx, ty);

		if (cd == 0) return false; // the points are equal
		if (md > (large ? 2 : 1)) return false; // outside the maximum range
		if (cd == 1) return target.test(world, tx, ty); // inside inner circle

		// at this point there are only 4 possible positions
		// each on an axis-aligned line starting from the (fx, fy) point and at a distance of 2 from it,
		// validity of the selection depends then on the tile in the middle between (fx, fy) and (tx, ty)

		final TilePos middle = MathHelper.getMiddlePoint(fx, fy, tx, ty);
		final Direction vector = MathHelper.getDirection(fx, fy, tx, ty);
		final Entity tile = world.getEntity(middle);

		if (tile instanceof Warp warp && warp.canWarpFrom(fx, fy)) {
			return warp.getWarpedTiles().contains(new TilePos(tx, ty));
		}

		return midpoint.test(world, vector, tile, middle) && target.test(world, tx, ty);
	}

	public interface TargetPredicate {
		boolean test(WorldSnapshot world, int x, int y);
	}

	public interface MidpointPredicate {
		boolean test(WorldSnapshot world, Direction direction, Entity tile, TilePos pos);
	}

	public static void iterateValid(WorldSnapshot world, TargetPredicate target, MidpointPredicate midpoint, boolean large, int fx, int fy, Consumer<TilePos> consumer) {
		Set<TilePos> linked = new HashSet<>();

		for (TilePos offset : Patterns.CROSS.getOffsets()) {
			int tx = fx + offset.x;
			int ty = fy + offset.y;
			consumeValidOffset(world, target, midpoint, large, fx, fy, tx, ty, consumer);

			if (world.getEntity(tx, ty) instanceof Warp warp && warp.isWarpDirect() && warp.canWarpFrom(fx, fy)) {
				linked.addAll(warp.getWarpedTiles());
			}
		}

		if (large) {
			for (TilePos offset : Patterns.HALO.getOffsets()) {
				consumeValidOffset(world, target, midpoint, true, fx, fy, fx + offset.x, fy + offset.y, consumer);
			}
		}

		linked.remove(new TilePos(fx, fy));
		for (TilePos pos : linked) {
			if (target.test(world, pos.x, pos.y)) {
				consumer.accept(pos);
			}
		}
	}

	private static void consumeValidOffset(WorldSnapshot world, TargetPredicate target, MidpointPredicate midpoint, boolean large, int fx, int fy, int tx, int ty, Consumer<TilePos> consumer) {
		if (isValid(world, target, midpoint, large, fx, fy, tx, ty)) {
			consumer.accept(new TilePos(tx, ty));
		}
	}

}
