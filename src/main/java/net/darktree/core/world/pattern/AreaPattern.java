package net.darktree.core.world.pattern;

import net.darktree.core.world.tile.TilePos;

import java.util.function.Consumer;

public class AreaPattern extends Pattern {

	private final int width;
	private final int height;

	AreaPattern(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	protected void forEachTile(Consumer<TilePos> consumer) {
		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				consumer.accept(new TilePos(x, y));
			}
		}
	}

}
