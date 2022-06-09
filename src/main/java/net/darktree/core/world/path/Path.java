package net.darktree.core.world.path;

import net.darktree.core.client.Colors;
import net.darktree.core.client.Sprites;
import net.darktree.core.client.render.color.Color;
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

	public TilePos getStart() {
		return this.positions.get(0);
	}

	public TilePos getEnd() {
		return this.positions.get(positions.size() - 1);
	}

	/**
	 * Draw this path as a series of lines and boxes
	 */
	public void draw(VertexBuffer buffer) {

		TilePos prev = positions.get(0);
		Color c = Colors.PATH;

		drawBox(buffer, prev, c);

		for (int i = 1; i < positions.size(); i ++) {
			TilePos pos = positions.get(i);

			Renderer.line(buffer, prev.x + 0.5f, prev.y + 0.5f, pos.x + 0.5f, pos.y + 0.5f, 0.05f, c);

			drawBox(buffer, pos, c);

			prev = pos;
		}

	}

	private void drawBox(VertexBuffer buffer, TilePos pos, Color c) {
		Renderer.quad(buffer, pos.x + 0.4f, pos.y + 0.4f, 0.2f, 0.2f, Sprites.NONE, c.r, c.g, c.b, c.a);
	}

}
