package net.darktree.core.world;

import net.darktree.core.util.Pair;
import net.darktree.core.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class PatternBuilder {

	private final List<Pair<Integer, Integer>> pairs = new ArrayList<>();

	PatternBuilder() {

	}

	public PatternBuilder add(int x, int y) {
		pairs.add(Pair.of(x, y));
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

	public Pattern build() {
		int[][] offsets = new int[pairs.size()][2];

		for (int i = 0; i < pairs.size(); i ++) {
			var pair = pairs.get(i);

			offsets[i][0] = pair.a;
			offsets[i][1] = pair.b;
		}

		return new Pattern(offsets);
	}

}
