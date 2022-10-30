package net.darktree.warzone.world.pattern;

import net.darktree.warzone.util.math.MathHelper;
import net.darktree.warzone.world.tile.TilePos;

import java.util.ArrayList;
import java.util.List;

public class PatternBuilder {

	private final List<TilePos> pairs = new ArrayList<>();

	PatternBuilder() {

	}

	public PatternBuilder add(int x, int y) {
		pairs.add(new TilePos(x, y));
		return this;
	}
	
	public PatternBuilder addManhattan(int size) {
		for (int x = -size; x <= size; x ++) {
			for (int y = -size; y <= size; y ++) {
				if (MathHelper.getManhattanDistance(0, 0, x, y) <= size) {
					add(x, y);
				}
			}
		}
		
		return this;
	}

	public FixedPattern build() {
		TilePos[] offsets = new TilePos[pairs.size()];

		for (int i = 0; i < pairs.size(); i ++) {
			offsets[i] = pairs.get(i);
		}

		return new FixedPattern(offsets);
	}

}
