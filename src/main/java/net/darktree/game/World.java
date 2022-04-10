package net.darktree.game;

import net.darktree.game.nbt.NbtSerializable;
import net.darktree.game.state.TileInstance;
import net.darktree.game.state.TileState;
import net.darktree.opengl.image.Atlas;
import net.darktree.opengl.image.Sprite;
import net.darktree.opengl.vertex.VertexBuffer;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

public class World implements NbtSerializable {

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
				this.tiles[x][y] = new TilePoint(null, null);
			}
		}
	}

	@Override
	public void toNbt(@NotNull CompoundTag tag) {
		CompoundTag tilesTag = new CompoundTag();

		for (int x = 0; x < this.width; x ++) {
			for (int y = 0; y < this.height; y ++) {
				CompoundTag tileTag = new CompoundTag();
				this.tiles[x][y].toNbt(tileTag);
				tilesTag.put(x + " " + y, tileTag);
			}
		}

		tag.putInt("width", this.width);
		tag.putInt("height", this.height);
		tag.put("tiles", tilesTag);
	}

	// should we implement NbtSerializable if that operation is unsupported?
	@Override
	public void fromNbt(@NotNull CompoundTag tag) {
		throw new UnsupportedOperationException("World can't be loaded after being created!");
	}

	public static World load(CompoundTag tag) {
		CompoundTag tilesTag = tag.getCompoundTag("tiles");
		World world = new World(tag.getInt("width"), tag.getInt("height"));

		for (int x = 0; x < world.width; x ++) {
			for (int y = 0; y < world.height; y ++) {
				world.tiles[x][y].load(world, x, y, tilesTag);
			}
		}

		return world;
	}

	public void loadTiles(Function<TilePos, TileState> generator) {
		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				this.setTileState(x, y, generator.apply(new TilePos(x, y)));
			}
		}
	}

	public void assertPosition(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			throw new IndexOutOfBoundsException("Position (" + x + ", " + y + ") is out of world bounds!");
		}
	}

	/**
	 *  @throws IndexOutOfBoundsException if the given position is invalid
	 */
	public TileState getTileState(int x, int y) {
		assertPosition(x, y);

		return this.tiles[x][y].state;
	}

	/**
	 *  @throws IndexOutOfBoundsException if the given position is invalid
	 */
	public void setTileState(int x, int y, TileState state) {
		assertPosition(x, y);

		TilePoint point = this.tiles[x][y];

		if (point.state != null) {
			point.state.getTile().onRemoved(this, x, y, state);

			if (point.instance != null) {
				point.instance = null;
			}
		}

		point.instance = state.getTile().getInstance(this, x, y);
		point.state = state;
	}

	/**
	 * Returns the tile instance of given type from the requested position,
	 * or null if there is no tile instance at that position, or if it is of incorrect type.
	 *
	 * @throws IndexOutOfBoundsException if the given position is invalid
	 */
	public <T extends TileInstance> T getTileInstance(int x, int y, Class<T> clazz) {
		assertPosition(x, y);

		TileInstance instance = this.tiles[x][y].instance;

		if (clazz.isInstance(instance)) {
			return clazz.cast(instance);
		}

		return null;
	}

	public void draw(VertexBuffer buffer) {
		atlas.texture.bind();

		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				this.tiles[x][y].state.getTile().draw(this, x, y, buffer);
			}
		}
	}

	@Deprecated
	void forEach(Consumer<TileState> consumer) {
		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				consumer.accept(this.tiles[x][y].state);
			}
		}
	}

	static final class TilePoint implements NbtSerializable {

		TileState state;
		TileInstance instance;

		TilePoint(TileState state, TileInstance instance) {
			this.state = state;
			this.instance = instance;
		}

		@Override
		public void toNbt(@NotNull CompoundTag tag) {
			state.toNbt(tag);
			tag.putString("id", Registries.TILES.getKey(state.getTile()));

			if (instance != null) {
				instance.toNbt(tag);
			}
		}

		@Override
		public void fromNbt(@NotNull CompoundTag tag) {
			state = Registries.TILES.getElement(tag.getString("id")).getDefaultState().fromNbt(tag);
		}

		public void load(World world, int x, int y, CompoundTag tag) {
			CompoundTag tile = tag.getCompoundTag(x + " " + y);
			fromNbt(tile);
			instance = state.getTile().getInstance(world, x, y);

			if (instance != null) {
				instance.fromNbt(tile);
			}
		}

	}

}
