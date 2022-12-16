package net.darktree.warzone.world.terrain;

import net.darktree.warzone.world.World;
import net.darktree.warzone.world.pattern.Pattern;

public class AbstractFieldFinder extends AbstractFinder {

	protected final int[][] field;

	protected AbstractFieldFinder(Pattern pattern, World world) {
		super(pattern, world);

		this.field = new int[this.width][this.height];
	}

	protected final void iterate(WorldIterator iterator) {
		int level = 1;

		for (boolean dirty = true; dirty; level ++) {
			dirty = false;

			for (int x = 0; x < width; x ++) {
				for (int y = 0; y < height; y ++) {
					if (field[x][y] == level) {
						dirty = true;
						iterator.accept(x, y, level + 1);
					}
				}
			}
		}
	}

	protected interface WorldIterator {
		void accept(int x, int y, int level);
	}

}
