package net.darktree.game;

import net.darktree.Main;
import net.darktree.game.tiles.EmptyTile;
import net.darktree.opengl.Window;
import net.darktree.opengl.image.Atlas;
import net.darktree.opengl.image.Image;
import net.darktree.opengl.image.Sprite;
import net.darktree.opengl.image.Texture;
import net.darktree.opengl.vertex.VertexBuffer;

import java.util.function.Function;

public class World {

	final private int width, height;
	final private Tile[][] tiles;

	final Sprite EMPTY;

	public float x, y, s;

	public World(int width, int height) {
		this.width = width;
		this.height = height;
		this.tiles = new Tile[width][height];

		this.loadTiles(pos -> new EmptyTile(this));

		// FIXME, let's not do it here
		Atlas atlas = Atlas.createEmpty();
		var ref = atlas.add("test.png");
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
		this.s = Window.INSTANCE.input().zoom;
		this.x = Window.INSTANCE.input().offsetX;
		this.y = Window.INSTANCE.input().offsetY;

		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				this.tiles[x][y].draw(buffer, this.x + x * this.s, this.y + y * this.s, this.s);
			}
		}
	}

}
