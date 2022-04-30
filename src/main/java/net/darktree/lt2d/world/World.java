package net.darktree.lt2d.world;

import net.darktree.Main;
import net.darktree.game.buildings.Building;
import net.darktree.game.country.TileOwner;
import net.darktree.game.tiles.Tiles;
import net.darktree.lt2d.Registries;
import net.darktree.lt2d.graphics.vertex.VertexBuffer;
import net.darktree.lt2d.util.NbtSerializable;
import net.darktree.lt2d.util.Type;
import net.darktree.lt2d.world.entities.Entity;
import net.darktree.lt2d.world.overlay.Overlay;
import net.darktree.lt2d.world.state.TileVariant;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class World implements NbtSerializable {

	final public int width, height;
	final private TileState[][] tiles;
	final private List<Entity> entities = new ArrayList<>();
	final private HashMap<TilePos, Building> buildings = new HashMap<>();
	private Overlay overlay = null;

	public World(int width, int height) {
		this.width = width;
		this.height = height;
		this.tiles = new TileState[width][height];

		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				this.tiles[x][y] = new TileState(null, null, new TileOwner());
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

	public static void load(CompoundTag tag) {
		CompoundTag tilesTag = tag.getCompoundTag("tiles");
		CompoundTag entitiesTag = tag.getCompoundTag("entities");
		World world = new World(tag.getInt("width"), tag.getInt("height"));

		Main.world = world;

		for (int x = 0; x < world.width; x ++) {
			for (int y = 0; y < world.height; y ++) {
				world.tiles[x][y].load(world, x, y, tilesTag);
			}
		}

		entitiesTag.forEach(entry -> {
			world.addEntity(Entity.load(world, (CompoundTag) entry.getValue()));
		});
	}

	public void loadTiles(Function<TilePos, TileVariant> generator) {
		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				this.setTileState(x, y, generator.apply(new TilePos(x, y)));
			}
		}
	}

	public boolean isPositionValid(int x, int y) {
		return x >= 0 && y >= 0 && x < width && y < height;
	}

	public void addEntity(Entity entity) {
		this.entities.add(entity);
	}

	public void addEntity(int x, int y, Type<Entity> type) {
		this.entities.add(type.construct(this, x, y));
	}

	public Entity getEntity(int x, int y) {
		return this.entities.stream().filter(entity -> entity.isAt(x, y)).findFirst().orElse(null);
	}

	public List<Entity> getEntities() {
		return entities;
	}

	/**
	 *  @throws IndexOutOfBoundsException if the given position is invalid
	 */
	public TileState getTileState(int x, int y) {
		if (isPositionValid(x, y)) {
			return this.tiles[x][y];
		} else {
			throw new IndexOutOfBoundsException("Position (" + x + ", " + y + ") is out of world bounds!");
		}
	}

	/**
	 *  @throws IndexOutOfBoundsException if the given position is invalid
	 */
	public void setTileState(int x, int y, TileVariant variant) {
		getTileState(x, y).setVariant(this, x, y, variant);
	}

	/**
	 * Returns the tile instance of given type from the requested position,
	 * or null if there is no tile instance at that position, or if it is of incorrect type.
	 *
	 * @throws IndexOutOfBoundsException if the given position is invalid
	 */
	public <T extends TileInstance> T getTileInstance(int x, int y, Class<T> clazz) {
		isPositionValid(x, y);

		TileInstance instance = this.tiles[x][y].getInstance();

		if (clazz.isInstance(instance)) {
			return clazz.cast(instance);
		}

		return null;
	}

	public void placeBuilding(int x, int y, Type<Building> type) {
		Building building = type.construct(this, x, y);

		building.getPattern().iterate(this, x, y, (pos) -> {
			TileState state = this.getTileState(pos.x, pos.y);
			state.setVariant(this, pos.x, pos.y, Tiles.STRUCTURE.getDefaultVariant());
			((Building.Link) state.getInstance()).linkWith(x, y);
		});

		setBuildingAt(x, y, building);
	}

	public void draw(VertexBuffer buffer) {
		this.entities.removeIf(entity -> entity.removed);

		Registries.ATLAS.texture.bind();

		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				TileState state = this.tiles[x][y];
				state.getTile().draw(x, y, state, buffer);
			}
		}

		this.entities.forEach(entity -> entity.draw(buffer));
	}

	public void getPatternTiles(Pattern pattern, int x, int y, Consumer<TileState> consumer) {
		pattern.iterate(this, x, y, pos -> consumer.accept(this.tiles[pos.x][pos.y]));
	}

	public Overlay getOverlay() {
		return this.overlay;
	}

	public void setOverlay(Overlay overlay) {
		this.overlay = overlay;
	}

	public Building getBuildingAt(int x, int y) {
		return buildings.get(new TilePos(x, y));
	}

	public void setBuildingAt(int x, int y, Building building) {
		buildings.put(new TilePos(x, y), building);
	}

}
