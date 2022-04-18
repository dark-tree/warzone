package net.darktree.lt2d.world.path;

import net.darktree.lt2d.world.TilePos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Path {

	private int index;
	private final List<TilePos> positions = new ArrayList<>();

	public void addTarget(int x, int y) {
		positions.add(new TilePos(x, y));
	}

	public void addTarget(TilePos pos) {
		positions.add(pos);
	}

	public void reverseAll() {
		Collections.reverse(positions);
	}

	public TilePos getNext() {
		if (index == positions.size()) {
			return null;
		}

		return this.positions.get(index ++);
	}

}
