package net.darktree.warzone.util.iterable;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class SpiralIterable implements Iterable<SpiralIterable.SpiralPoint> {

	// based on https://stackoverflow.com/a/8979620
	private static final class SpiralIterator implements Iterator<SpiralPoint> {

		private final int max, x, y;
		private int r, i, j, s;

		public SpiralIterator(int max, int x, int y) {
			this.max = max;
			this.r = 1;
			this.i = x - 1;
			this.j = y - 1;
			this.s = 0;
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean hasNext() {
			return r < max;
		}

		@Override
		public SpiralPoint next() {
			if (s == 0) {
				if (i < x + r) {
					i ++;
					return new SpiralPoint(r, i, j);
				}

				s = 1;
			}

			if (s == 1) {
				if (j < y + r) {
					j ++;
					return new SpiralPoint(r, i, j);
				}

				s = 2;
			}

			if (s == 2) {
				if (i > x - r) {
					i --;
					return new SpiralPoint(r, i, j);
				}

				s = 3;
			}

			if (s == 3) {
				if (j > y - r) {
					j --;
					return new SpiralPoint(r, i, j);
				}

				s = 0;
				r ++;
				j --;
			}

			return new SpiralPoint(r, i, j);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	public static class SpiralPoint {
		public final int x, y, r;

		private SpiralPoint(int r, int x, int y) {
			this.x = x;
			this.y = y;
			this.r = r;
		}

		@Override
		public String toString() {
			return "SpiralPoint{x=" + x + ", y=" + y + ", r=" + r + "}";
		}
	}

	private final int radius, x, y;

	private SpiralIterable(int radius, int x, int y) {
		this.radius = radius;
		this.x = x;
		this.y = y;
	}

	/**
	 * Create a spiral iterator that starts at the given point
	 */
	public static SpiralIterable of(int radius, int x, int y) {
		return new SpiralIterable(radius, x, y);
	}

	@NotNull
	@Override
	public Iterator<SpiralPoint> iterator() {
		return new SpiralIterator(radius, x, y);
	}

}