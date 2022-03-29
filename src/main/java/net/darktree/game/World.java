package net.darktree.game;

import net.darktree.game.tiles.EmptyTile;
import net.darktree.opengl.Window;
import net.darktree.opengl.image.Atlas;
import net.darktree.opengl.image.Sprite;
import net.darktree.opengl.vertex.VertexBuffer;

import java.util.function.Function;

public class World {

	final public int width, height;
	final private Tile[][] tiles;

	public final Sprite EMPTY, CIRCLE, CROSS, DELETED;

	public float x, y, s;
	public boolean circle = true;

	Atlas atlas;

	public World(int width, int height) {
		this.width = width;
		this.height = height;
		this.tiles = new Tile[width][height];

		this.loadTiles(pos -> new EmptyTile(this, pos.x, pos.y));

		// FIXME, let's not do it here
		atlas = Atlas.createEmpty();
		var ref1 = atlas.add("sprites/empty.png");
		var ref2 = atlas.add("sprites/circle-2.png");
		var ref3 = atlas.add("sprites/cross.png");
		var ref4 = atlas.add("sprites/deleted.png");
		atlas.freeze();
		atlas.texture.upload();

		EMPTY = ref1.sprite();
		CIRCLE = ref2.sprite();
		CROSS = ref3.sprite();
		DELETED = ref4.sprite();

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
		atlas.texture.bind();

		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				this.tiles[x][y].draw(buffer, x, y);
			}
		}
	}

}
