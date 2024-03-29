package net.darktree.warzone.world.pattern;

import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.tile.TilePos;

import java.util.List;
import java.util.function.Consumer;

public class PlacedPattern implements PlacedTileIterator {

	private final WorldSnapshot world;
	private final int x;
	private final int y;
	private final Pattern pattern;

	public PlacedPattern(WorldSnapshot world, int x, int y, Pattern pattern) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.pattern = pattern;
	}

	@Override
	public void iterate(Consumer<TilePos> consumer) {
		pattern.iterate(world, x, y, consumer);
	}

	public List<TilePos> list(boolean required) {
		return pattern.list(world, x, y, required);
	}

	public Pattern getPattern() {
		return pattern;
	}

}
