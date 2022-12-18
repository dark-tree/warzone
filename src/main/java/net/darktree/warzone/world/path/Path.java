package net.darktree.warzone.world.path;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.vertex.Renderer;
import net.darktree.warzone.client.render.vertex.VertexBuffer;
import net.darktree.warzone.world.tile.TilePos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Path {

	private int index;
	private final List<TilePos> positions = new ArrayList<>();

	/**
	 * Add a node to the path
	 */
	public void addTarget(int x, int y) {
		positions.add(new TilePos(x, y));
	}

	/**
	 * Add a node to the path
	 */
	public void addTarget(TilePos pos) {
		positions.add(pos);
	}

	/**
	 * Reverse the order of all nodes in this path
	 */
	public void reverseAll() {
		Collections.reverse(positions);
	}

	/**
	 * Get the starting position
	 */
	public TilePos getStart() {
		return this.positions.get(0);
	}

	/**
	 * Get the end position
	 */
	public TilePos getEnd() {
		return this.positions.get(positions.size() - 1);
	}

	/**
	 * Get the next node in this path, calling it repeatedly
	 * will return all the subsequent path nodes until
	 * the end is reached, then null will be returned.
	 */
	public TilePos getNext() {
		if (index == positions.size()) {
			return null;
		}

		return this.positions.get(index ++);
	}

	/**
	 * Draw this path as a series of lines and boxes into the given buffer
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
