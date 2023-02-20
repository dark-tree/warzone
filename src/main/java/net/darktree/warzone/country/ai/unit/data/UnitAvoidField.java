package net.darktree.warzone.country.ai.unit.data;

public final class UnitAvoidField {

	private final int width, height;
	private final int[][] field;

	public UnitAvoidField(int width, int height) {
		this.width = width;
		this.height = height;
		this.field = new int[width][height];
	}

	public int get(int x, int y) {
		return field[x][y];
	}

	public void set(int x, int y, int weight) {
		field[x][y] = weight;
	}

	public void clear() {
		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				field[x][y] = 0;
			}
		}
	}

}
