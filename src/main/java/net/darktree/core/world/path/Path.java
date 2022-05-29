package net.darktree.core.world.path;

import net.darktree.core.client.Sprites;
import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.world.tile.TilePos;

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

	public TilePos getStarting() {
		return this.positions.get(0);
	}

	/**
	 * Draw this path as a series of lines and boxes
	 */
	public void draw(VertexBuffer buffer) {

		TilePos prev = positions.get(0);

		drawBox(buffer, prev);

		for (int i = 1; i < positions.size(); i ++) {
			TilePos pos = positions.get(i);

			Renderer.line(buffer, prev.x + 0.5f, prev.y + 0.5f, pos.x + 0.5f, pos.y + 0.5f, 0.05f, 1, 1, 1, 0.95f);

			drawBox(buffer, pos);

			prev = pos;
		}

	}

	private void drawBox(VertexBuffer buffer, TilePos pos) {
		Renderer.quad(buffer, pos.x + 0.4f, pos.y + 0.4f, 0.2f, 0.2f, Sprites.NULL, 1, 1, 1, 0.95f);
	}

}
