package net.darktree.core.world;

import net.darktree.Main;
import net.darktree.core.Registries;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.client.window.Input;
import net.darktree.core.event.TurnEvent;
import net.darktree.core.util.NbtSerializable;
import net.darktree.core.util.Type;
import net.darktree.core.world.entity.Entity;
import net.darktree.core.world.overlay.Overlay;
import net.darktree.core.world.tile.TileInstance;
import net.darktree.core.world.tile.TilePos;
import net.darktree.core.world.tile.TileState;
import net.darktree.core.world.tile.TileStateConsumer;
import net.darktree.core.world.tile.variant.TileVariant;
import net.darktree.core.world.view.WorldEntityView;
import net.darktree.game.buildings.Building;
import net.darktree.game.country.Country;
import net.darktree.game.country.Symbol;
import net.darktree.game.tiles.Tiles;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class World implements NbtSerializable, WorldEntityView {

	final public int width, height;
	final private TileState[][] tiles;
	final private List<Entity> entities = new ArrayList<>();
	final private List<Symbol> symbols = new ArrayList<>();
	final private HashMap<TilePos, Building> buildings = new HashMap<>();
	final private HashMap<Symbol, Country> countries = new HashMap<>();

	private Overlay overlay = null;
	private int turn;

	public float offsetX;
	public float offsetY;
	public float scaleX;
	public float scaleY;
	public float zoom;

	public World(int width, int height) {
		this.width = width;
		this.height = height;
		this.tiles = new TileState[width][height];

		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				this.tiles[x][y] = new TileState(null, null, Symbol.NONE);
			}
		}

		setZoom(Input.MAP_ZOOM_MIN * 1.8f);

		offsetX = width / -2f;
		offsetY = height / -2f;
	}

	public void drag(float x, float y) {
		this.offsetX += x / scaleX;
		this.offsetY += y / scaleY;
	}

	public void setZoom(float zoom) {
		this.scaleX = zoom * Main.window.height() / (float) Main.window.width();
		this.scaleY = zoom;
		this.zoom = zoom;
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

	public List<Entity> getEntities() {
		return entities;
	}

	/**
	 * Get TileState at given position
	 *
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
	 * Set tile variant at the given position
	 *
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
	 * of all the required setup. Returns true on success, false otherwise.
	 */
	public boolean placeBuilding(int x, int y, Type<Building> type) {
		Building building = type.construct(this, x, y);

		List<TilePos> tiles = building.getPattern().list(this, x, y, true);

		if (tiles.stream().allMatch(pos -> this.tiles[pos.x][pos.y].getTile().isReplaceable())) {
			tiles.forEach(pos -> {
				TileState state = this.tiles[pos.x][pos.y];
				state.setVariant(this, pos.x, pos.y, Tiles.STRUCTURE.getDefaultVariant(), true);
				((Building.Link) state.getInstance()).linkWith(x, y);
			});

			setLinkedBuildingAt(x, y, building);
			return true;
		}

		return false;
	}

	public void draw(VertexBuffer buffer) {
		this.entities.removeIf(entity -> entity.removed);

		Registries.ATLAS.getTexture().bind();

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

		Symbol symbol = getCurrentSymbol();
		sendPlayerTurnEvent(TurnEvent.TURN_END, symbol);

		turn = (turn + 1) % len;

		if (turn == 0) {
			sendPlayerTurnEvent(TurnEvent.TURN_CYCLE_END, symbol);
		}

		sendPlayerTurnEvent(TurnEvent.TURN_START, getCurrentSymbol());
	}

	private void sendPlayerTurnEvent(TurnEvent event, Symbol symbol) {
		forEach((state, x, y) -> state.getTile().onPlayerTurnEvent(this, x, y, event, symbol));
		getEntities().forEach(entity -> entity.onPlayerTurnEvent(this, entity.getX(), entity.getY(), event, symbol));
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
