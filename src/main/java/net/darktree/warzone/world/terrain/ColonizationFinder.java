package net.darktree.warzone.world.terrain;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.country.ai.WeighedPos;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.UnitEntity;
import net.darktree.warzone.world.path.Path;
import net.darktree.warzone.world.path.PathFinder;
import net.darktree.warzone.world.path.PathFinderConfig;
import net.darktree.warzone.world.pattern.Patterns;
import net.darktree.warzone.world.pattern.PlacedTileIterator;
import net.darktree.warzone.world.tile.TilePos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ColonizationFinder extends AbstractFinder {

	private final List<WeighedPos> targets;
	private final Symbol self;
	private final boolean allowRandom;
	private PathFinder immediate, deferred;

	public ColonizationFinder(Symbol self, World world, List<WeighedPos> targets, boolean allowRandom) {
		super(Patterns.NEIGHBOURS, world);
		this.targets = targets;
		this.self = self;
		this.allowRandom = allowRandom;

		update();
	}

	private PlacedTileIterator createIterator(Symbol symbol, BorderFinder border, boolean unit) {
		return iterator -> border.getBorderTiles(symbol).stream().filter(pos -> isGuardAt(pos) == unit).forEach(iterator);
	}

	public void update() {
		this.immediate = new PathFinder(world, self, createIterator(self, world.getBorder(), true), PathFinderConfig.COLONIZATION, false);
		this.deferred = new PathFinder(world, self, createIterator(self, world.getBorder(), false), PathFinderConfig.COLONIZATION, false);
	}

	/**
	 * Get the colonization targets, colonization flag indicates that the returned values
	 * will be colonization spots, setting it to false will make the returned values movement targets
	 */
	public List<WeighedPos> solve(boolean colonization) {
		Collections.sort(targets);
		List<WeighedPos> actions = new ArrayList<>();
		boolean inactive = true;

		for (WeighedPos target : targets) {
			int min = Integer.MAX_VALUE;
			boolean colonized = false;
			WeighedPos action = null;

			for (PathFinder finder : List.of(immediate, deferred)) {
				int x = target.x;
				int y = target.y;

				if (finder.canReach(x, y)) {
					Path path = finder.getPathTo(x, y);
					boolean primary = (finder == immediate);
					int steps = path.getLength();
					int length = steps - (primary ? 1 : 0);

					if (min >= length && steps > 1) {
						min = length;
						action = target.childPos(path.getStart());
						colonized = primary;
					}
				}
			}

			// -> if colonized is now set to FALSE that means
			// that it's better to wait to the next turn and instead place a new unit closer to the target
			// adding 'action' position to the action list will tell the AI to place a unit on that tile if possible

			// -> if colonized is now set to TRUE that means
			// that it's better to colonize with the already placed unit for this target
			// adding 'action' position to the action list will tell the AI to attempt colonization from that tile

			if (colonized) {
				inactive = false;
			}

			if (colonized == colonization && action != null) {
				actions.add(action);
			}
		}

		// all actions where postponed (or no immediate source was found)
		// try to colonize anyway to now waste the move, (if enabled)

		if (colonization && inactive && allowRandom) {
			int max = 0;
			WeighedPos selected = null;

			for (TilePos unit : world.getBorder().getBorderTiles(self)) {
				int gain = getGain(unit.x, unit.y);
				if (isGuardAt(unit) && gain > max) {
					selected = WeighedPos.wrap(unit, 1);
					max = gain;
				}
			}

			if (selected != null) {
				actions.add(selected);
			}
		}

		return actions;
	}

	private boolean isGuardAt(TilePos pos) {
		if (getEntity(pos.x, pos.y) instanceof UnitEntity unit) {
			return unit.getSymbol() == self;
		}

		return false;
	}

}
