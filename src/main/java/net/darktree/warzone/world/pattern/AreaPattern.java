package net.darktree.warzone.world.pattern;

import net.darktree.warzone.world.tile.TilePos;

public class AreaPattern extends FixedPattern {

	AreaPattern(int width, int height) {
		super(new TilePos[width * height]);

		int i = 0;

		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				offsets[i ++] = new TilePos(x, y);
			}
		}
	}

}
