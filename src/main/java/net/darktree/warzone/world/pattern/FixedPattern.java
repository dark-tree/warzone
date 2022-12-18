package net.darktree.warzone.world.pattern;

import net.darktree.warzone.world.tile.TilePos;

public class FixedPattern extends Pattern {

	protected final TilePos[] offsets;

	FixedPattern(TilePos[] offsets) {
		this.offsets = offsets;
	}

	@Override
	public TilePos[] getOffsets() {
		return offsets;
	}

}
