package net.darktree.warzone.world.pattern;

import net.darktree.warzone.util.Direction;
import net.darktree.warzone.util.math.MathHelper;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.tile.TilePos;

import java.util.function.Consumer;

public class ShapeHelper {

	public static boolean isValid(World world, TargetPredicate target, MidpointPredicate midpoint, boolean large, int fx, int fy, int tx, int ty) {
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

		return midpoint.test(world, vector, tile, middle) && target.test(world, tx, ty);
	}

	public interface TargetPredicate {
		boolean test(World world, int x, int y);
	}

	public interface MidpointPredicate {
		boolean test(World world, Direction direction, Entity tile, TilePos pos);
	}

	public static void iterateValid(World world, TargetPredicate target, MidpointPredicate midpoint, boolean large, int fx, int fy, Consumer<TilePos> consumer) {
		Consumer<TilePos> offsetConsumer = pos -> {
			int tx = fx + pos.x;
			int ty = fy + pos.y;

			if (isValid(world, target, midpoint, large, fx, fy, tx, ty)) {
				consumer.accept(new TilePos(tx, ty));
			}
		};

		Patterns.SMALL_CROSS.forEachOffset(offsetConsumer);

		if (large) {
			Patterns.STAR.forEachOffset(offsetConsumer);
		}
	}

}
