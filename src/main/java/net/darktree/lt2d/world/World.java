package net.darktree.lt2d.world;

import net.darktree.lt2d.Registries;
import net.darktree.lt2d.graphics.vertex.VertexBuffer;
import net.darktree.lt2d.util.NbtSerializable;
import net.darktree.lt2d.world.state.TileState;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class World implements NbtSerializable {

	final public int width, height;
	final private TilePoint[][] tiles;
	final private List<Entity> entities = new ArrayList<>();

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

		CompoundTag entitiesTag = new CompoundTag();

		for (int i = 0; i < this.entities.size(); i ++) {
			CompoundTag entityTag = new CompoundTag();
			this.entities.get(i).toNbt(entityTag);
			entitiesTag.put(String.valueOf(i), entityTag);
		}

		tag.putInt("width", this.width);
		tag.putInt("height", this.height);
		tag.put("tiles", tilesTag);
		tag.put("entities", entitiesTag);
	}

	// should we implement NbtSerializable if that operation is unsupported?
	@Override
	public void fromNbt(@NotNull CompoundTag tag) {
		throw new UnsupportedOperationException("World can't be loaded after being created!");
	}

	public static World load(CompoundTag tag) {
		CompoundTag tilesTag = tag.getCompoundTag("tiles");
		CompoundTag entitiesTag = tag.getCompoundTag("entities");
		World world = new World(tag.getInt("width"), tag.getInt("height"));

		for (int x = 0; x < world.width; x ++) {
			for (int y = 0; y < world.height; y ++) {
				world.tiles[x][y].load(world, x, y, tilesTag);
			}
		}

		entitiesTag.forEach(entry -> {
			world.addEntity(Entity.load(world, (CompoundTag) entry.getValue()));
		});

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

	public void addEntity(Entity entity) {
		this.entities.add(entity);
	}

	public void addEntity(int x, int y, Entity.Type type) {
		this.entities.add(type.construct(this, x, y));
	}

	public Entity getEntity(int x, int y) {
		return this.entities.stream().filter(entity -> entity.isAt(x, y)).findFirst().orElse(null);
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
		this.entities.removeIf(entity -> entity.removed);

		Registries.ATLAS.texture.bind();

		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				this.tiles[x][y].state.getTile().draw(x, y, buffer);
			}
		}

		this.entities.forEach(entity -> entity.draw(buffer));
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
