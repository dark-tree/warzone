package net.darktree.warzone.world.path;

import net.darktree.warzone.world.tile.TilePos;

public class PathProvider {

	private final PathFinder finder;
	private final Path path;

	public PathProvider(PathFinder finder, Path path) {
		this.finder = finder;
		this.path = path;
	}

	/**
	 * Get a full path represented by this provider
	 */
	public Path getFullPath() {
		return path;
	}

	/**
	 * Get the longest valid subsection of the underlying path
	 */
	public Path getValidPath() {
		int found = -1;
		int index = 0;

		while (true) {
			TilePos pos = path.getNext();

			if (pos == null) {
				break;
			}

			if (finder.canReach(pos.x, pos.y)) {
				found = index;
			}

			index ++;
		}

		if (found == -1) {
			return null;
		}

		// only needed here so that getFullPath() still works as expected afterwords
		path.rewind();

		return path.getSubPath(found);
	}

}
