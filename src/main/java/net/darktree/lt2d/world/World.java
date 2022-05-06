package net.darktree.lt2d.world;

import net.darktree.Main;
import net.darktree.game.buildings.Building;
import net.darktree.game.country.Country;
import net.darktree.game.country.Symbol;
import net.darktree.game.tiles.Tiles;
import net.darktree.lt2d.Registries;
import net.darktree.lt2d.graphics.vertex.VertexBuffer;
import net.darktree.lt2d.util.NbtSerializable;
import net.darktree.lt2d.util.TileStateConsumer;
import net.darktree.lt2d.util.Type;
import net.darktree.lt2d.world.entities.Entity;
import net.darktree.lt2d.world.overlay.Overlay;
import net.darktree.lt2d.world.variant.TileVariant;
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
	final private List<Symbol> symbols = new ArrayList<>();
	final private HashMap<TilePos, Building> buildings = new HashMap<>();
	final private HashMap<Symbol, Country> countries = new HashMap<>();

	private Overlay overlay = null;
	private int turn;

	public World(int width, int height) {
		this.width = width;
		this.height = height;
		this.tiles = new TileState[width][height];

		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				this.tiles[x][y] = new TileState(null, null, Symbol.NONE);
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

		CompoundTag countriesTag = new CompoundTag();

		for (Symbol symbol : Symbol.values()) {
			CompoundTag countryTag = new CompoundTag();

			Country country = this.countries.get(symbol);
			if (country != null) {
				country.toNbt(countryTag);
				countriesTag.put(symbol.name(), countryTag);
			}
		}

		tag.putInt("width", this.width);
		tag.putInt("height", this.height);
		tag.putByte("turn", (byte) turn);
		tag.put("tiles", tilesTag);
		tag.put("entities", entitiesTag);
		tag.put("countries", countriesTag);
	}

	// should we implement NbtSerializable if that operation is unsupported?
	@Override
	public void fromNbt(@NotNull CompoundTag tag) {
		throw new UnsupportedOperationException("World can't be loaded after being created!");
	}

	public static void load(CompoundTag tag) {
		CompoundTag tilesTag = tag.getCompoundTag("tiles");
		CompoundTag entitiesTag = tag.getCompoundTag("entities");
		CompoundTag countriesTag = tag.getCompoundTag("countries");
		World world = new World(tag.getInt("width"), tag.getInt("height"));
		world.turn = tag.getByte("turn");

		Main.world = world;

		for (int x = 0; x < world.width; x ++) {
			for (int y = 0; y < world.height; y ++) {
				world.tiles[x][y].load(world, x, y, tilesTag);
			}
		}

		for (Symbol symbol : Symbol.values()) {
			CompoundTag countryTag = countriesTag.getCompoundTag(symbol.name());

			if (countryTag != null) {
				world.defineCountry(symbol).fromNbt(countryTag);
				world.symbols.add(symbol);
			}
		}

		entitiesTag.forEach(entry -> {
			world.addEntity(Entity.load(world, (CompoundTag) entry.getValue()));
		});
	}

	public void loadTiles(Function<TilePos, TileVariant> generator) {
		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				this.getTileState(x, y).setVariant(this, x, y, generator.apply(new TilePos(x, y)), false);
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
		getTileState(x, y).setVariant(this, x, y, variant, true);
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

	/**
	 * Method used for placing buildings on the map, it takes care
	 * of all the required setup.
	 */
	public void placeBuilding(int x, int y, Type<Building> type) {
		Building building = type.construct(this, x, y);

		// FIXME: verify that the placement position is valid (enough space for the whole building)

		building.getPattern().iterate(this, x, y, (pos) -> {
			TileState state = this.getTileState(pos.x, pos.y);
			state.setVariant(this, pos.x, pos.y, Tiles.STRUCTURE.getDefaultVariant(), true);
			((Building.Link) state.getInstance()).linkWith(x, y);
		});

		setLinkedBuildingAt(x, y, building);
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

	public Country defineCountry(Symbol symbol) {
		Country country = new Country(symbol);
		countries.put(symbol, country);
		return country;
	}

	/**
	 * Get the symbol of current active player,
	 * returns null if there was an issue.
	 */
	public Symbol getCurrentSymbol() {
		try {
			return this.symbols.get(turn);
		}catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * Advance the game to the next player and sends map updates
	 */
	public void nextPlayerTurn() {
		int len = this.symbols.size();

		Symbol oldSymbol = getCurrentSymbol();
		forEach((state, x, y) -> state.getTile().onPlayerTurnEnd(this, x, y, oldSymbol));

		turn = (turn + 1) % len;
		Symbol newSymbol = getCurrentSymbol();

		if (turn == 0) {
			forEach((state, x, y) -> state.getTile().onTurnCycleEnd(this, x, y));
		}

		forEach((state, x, y) -> state.getTile().onPlayerTurnStart(this, x, y, newSymbol));
	}

	public void forEach(TileStateConsumer consumer) {
		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				consumer.accept(this.tiles[x][y], x, y);
			}
		}
	}

	public Country getCountry(Symbol symbol) {
		return countries.get(symbol);
	}

	public Overlay getOverlay() {
		return this.overlay;
	}

	public void setOverlay(Overlay overlay) {
		this.overlay = overlay;
	}

	public Building getLinkedBuildingAt(int x, int y) {
		return buildings.get(new TilePos(x, y));
	}

	public void setLinkedBuildingAt(int x, int y, Building building) {
		buildings.put(new TilePos(x, y), building);
	}
}
