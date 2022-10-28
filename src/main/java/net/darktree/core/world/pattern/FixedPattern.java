package net.darktree.core.world.pattern;

import net.darktree.core.world.tile.TilePos;

import java.util.function.Consumer;

public class FixedPattern extends Pattern {

	public final TilePos[] offsets;

	FixedPattern(TilePos[] offsets) {
		this.offsets = offsets;
	}

	public static PatternBuilder create() {
		return new PatternBuilder();
	}

	@Override
	protected void forEachTile(Consumer<TilePos> consumer) {
		for (TilePos pos : offsets) consumer.accept(pos);
	}

}
