package net.darktree.game;

import net.darktree.game.state.TileState;
import net.darktree.opengl.image.Atlas;
import net.darktree.opengl.image.Sprite;
import net.darktree.opengl.vertex.VertexBuffer;

import java.util.function.Consumer;
import java.util.function.Function;

public class World /*implements NbtSerializable*/ {

	final public int width, height;
	final private TilePoint[][] tiles;

	static Atlas atlas;

	public static Sprite EMPTY, CIRCLE, CROSS, DELETED;

	public static void init() {
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

	public static boolean circle = true;

	public World(int width, int height) {
		this.width = width;
		this.height = height;
		this.tiles = new TilePoint[width][height];

		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				this.tiles[x][y] = new TilePoint(null);
			}
		}
	}

	// FIXME
//	@Override
//	public void toNbt(@NotNull CompoundTag tag) {
//		CompoundTag tilesTag = new CompoundTag();
//
//		TilePallet pallet = new TilePallet();
//		forEach(tile -> {
//			CompoundTag tileTag = new CompoundTag();
//
//			tileTag.putInt("id", pallet.add(tile.getTile()));
//			tile.toNbt(tileTag);
//			tilesTag.put(tile.x + " " + tile.y, tileTag);
//		});
//
//		CompoundTag palletTag = new CompoundTag();
//		pallet.toNbt(palletTag);
//		tag.put("pallet", palletTag);
//
//		tag.putInt("width", this.width);
//		tag.putInt("height", this.height);
//		tag.put("tiles", tilesTag);
//	}
//
//	@Override
//	public void fromNbt(@NotNull CompoundTag tag) {
//		throw new UnsupportedOperationException("World can't be loaded after being created!");
//	}
//
//	public static World load(CompoundTag tag) {
//		TilePallet pallet = new TilePallet();
//		pallet.fromNbt(tag.getCompoundTag("pallet"));
//
//		CompoundTag tilesTag = tag.getCompoundTag("tiles");
//		World world = new World(tag.getInt("width"), tag.getInt("height"));
//
//		for (int x = 0; x < world.width; x ++) {
//			for (int y = 0; y < world.height; y ++) {
//				CompoundTag tileTag = tilesTag.getCompoundTag(x + " " + y);
//				world.tiles[x][y] = pallet.get(tileTag.getInt("id"), tileTag, world, x, y);
//			}
//		}
//
//		return world;
//	}

	public void loadTiles(Function<TilePos, TileState> generator) {
		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				this.setTileState(x, y, generator.apply(new TilePos(x, y)));
			}
		}
	}

	public TileState getTileState(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			return null; // TODO: consider throwing
		}

		return this.tiles[x][y].state;
	}

	// FIXME: this is bad
	public void setTileState(int x, int y, TileState state) {
		if (this.tiles[x][y].state != null) {
			this.tiles[x][y].state.getTile().onRemoved(this, x, y, state);
		}

		this.tiles[x][y].state = state;
	}

	public void draw(VertexBuffer buffer) {
		atlas.texture.bind();

		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				this.tiles[x][y].state.getTile().draw(this, x, y, buffer);
			}
		}
	}

	void forEach(Consumer<TileState> consumer) {
		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				consumer.accept(this.tiles[x][y].state);
			}
		}
	}

	static final class TilePoint {
		TileState state;

		TilePoint(TileState state /*, TileInstance instance*/) {
			this.state = state;
		}
	}

}
