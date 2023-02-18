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
	private final List<TilePos> positions;

	private Path(List<TilePos> positions) {
		this.positions = positions;
	}

	public static Recorder getRecorder() {
		return new Recorder(new ArrayList<>());
	}

	/**
	 * Return to the beginning of the path
	 */
	public void rewind() {
		index = 0;
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
	 * the end is reached, after that null will be returned.
	 */
	public TilePos getNext() {
		if (index == positions.size()) {
			return null;
		}

		return this.positions.get(index ++);
	}

	/**
	 * Get the number of nodes in this path
	 */
	public int getLength() {
		return positions.size();
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

	/**
	 * Get a path subsection from the beginning up to (and including) index
	 */
	public Path getSubPath(int index) {
		Recorder recorder = new Recorder(new ArrayList<>(index + 1));

		for (int i = 0; i <= index; i ++) {
			recorder.addTarget(positions.get(i));
		}

		return recorder.build();
	}

	private void drawBox(VertexBuffer buffer, TilePos pos, Color c) {
		Renderer.quad(buffer, pos.x + 0.4f, pos.y + 0.4f, 0.2f, 0.2f, Sprites.NONE, c.r, c.g, c.b, c.a);
	}

	public static class Recorder {

		private final List<TilePos> positions;

		private Recorder(List<TilePos> positions) {
			this.positions = positions;
		}

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
		public Recorder reverse() {
			Collections.reverse(positions);
			return this;
		}

		/**
		 * Create a path instance
		 */
		public Path build() {
			return new Path(positions);
		}

	}

}
