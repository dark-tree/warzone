package net.darktree.game;

import net.darktree.game.tiles.EmptyTile;
import net.darktree.opengl.image.Atlas;
import net.darktree.opengl.image.Sprite;
import net.darktree.opengl.vertex.VertexBuffer;

import java.util.function.Function;

public class World {

	final private int width, height;
	final private Tile[][] tiles;

	final Sprite EMPTY;

	public World(int width, int height) {
		this.width = width;
		this.height = height;
		this.tiles = new Tile[width][height];

		this.loadTiles(pos -> new EmptyTile(this));

		// FIXME, let's not do it here
		Atlas atlas = Atlas.createEmpty();
		var ref = atlas.add("empty.png");
		atlas.freeze();
		atlas.texture.upload();

		EMPTY = ref.sprite();

	}

	public void loadTiles(Function<TilePos, Tile> generator) {
		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				this.tiles[x][y] = generator.apply(new TilePos(x, y));
			}
		}
	}

	public Tile getTile(int x, int y) {
		return this.tiles[x][y];
	}

	public void draw(VertexBuffer buffer) {
		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				this.tiles[x][y].draw(buffer, x * 0.1f, y * 0.1f, 0.1f);
			}
		}
	}

}
