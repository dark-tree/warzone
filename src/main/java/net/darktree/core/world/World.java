package net.darktree.core.world;

import net.darktree.core.client.Colors;
import net.darktree.core.client.render.color.Color;
import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.event.TurnEvent;
import net.darktree.core.util.NbtSerializable;
import net.darktree.core.world.action.TaskManager;
import net.darktree.core.world.entity.Entity;
import net.darktree.core.world.overlay.Overlay;
import net.darktree.core.world.terrain.ControlFinder;
import net.darktree.core.world.terrain.EnclaveFinder;
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
	final private HashMap<TilePos, Building> buildings = new HashMap<>();
	final private HashMap<Symbol, Country> countries = new HashMap<>();

	private ControlFinder control;
	private Symbol[] symbols = new Symbol[]{};
	final private TaskManager manager = new TaskManager(this);
	private Overlay overlay = null;
	private boolean ownershipDirty = true;
	private int turn;

	private final WorldView view;

	public World(int width, int height) {
		this.width = width;
		this.height = height;
		this.tiles = new TileState[width][height];

		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				this.tiles[x][y] = new TileState(null, null, Symbol.NONE);
			}
		}

		this.view = new WorldView(width, height);
		this.control = new ControlFinder(this);
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

		WorldHolder.world = world;

		List<Symbol> symbols = new ArrayList<>();

		for (Symbol symbol : Symbol.values()) {
			CompoundTag countryTag = countriesTag.getCompoundTag(symbol.name());

			if (countryTag != null) {
				world.defineCountry(symbol).fromNbt(countryTag);
				symbols.add(symbol);
			}
		}

		world.symbols = symbols.toArray(new Symbol[]{});

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
				this.getTileState(x, y).setVariant(this, x, y, generator.apply(new TilePos(x, y)));
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
	public void setTileVariant(int x, int y, TileVariant variant) {
		getTileState(x, y).setVariant(this, x, y, variant);
	}

	/**
	 * Set tile owner at the given position
	 *
	 *  @throws IndexOutOfBoundsException if the given position is invalid
	 */
	public void setTileOwner(int x, int y, Symbol variant) {
		getTileState(x, y).setOwner(this, x, y, variant, true);
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
	public void placeBuilding(int x, int y, Building building) {
		List<TilePos> tiles = building.getPattern().list(this, x, y, true);

		tiles.forEach(pos -> {
			TileState state = this.tiles[pos.x][pos.y];
			state.setVariant(this, pos.x, pos.y, Tiles.STRUCTURE.getDefaultVariant());
			((Building.Link) state.getInstance()).linkWith(x, y);
		});

		setLinkedBuildingAt(x, y, building);
	}

	public void draw(VertexBuffer buffer) {
		this.entities.removeIf(entity -> entity.removed);

		// TODO: bake country borders

		if (ownershipDirty) {
			this.control = new ControlFinder(this);
			EnclaveFinder finder = new EnclaveFinder(this, this.control);

			finder.getEnclaves().forEach(enclave -> {
				Symbol symbol = enclave.encircled();

				if (symbol != Symbol.NONE) {
					enclave.forEachTile(pos -> {
						setTileOwner(pos.x, pos.y, symbol);
					});
				}
			});
		}

		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				TileState state = this.tiles[x][y];
				state.getTile().draw(this, x, y, state, buffer);

				drawBorders(buffer, x, y);
			}
		}

		this.entities.forEach(entity -> entity.draw(buffer));
		ownershipDirty = false;
	}

	private void drawBorders(VertexBuffer buffer, int x, int y) {
		Symbol self = tiles[x][y].getOwner();
		float w = 0.02f;
		Color c = Colors.BORDER;

		if (x != 0 && tiles[x - 1][y].getOwner() != self) {
			Renderer.line(buffer, x, y, x, y + 1, w, c.r, c.g, c.b, c.a);
		}

		if (y != 0 && tiles[x][y - 1].getOwner() != self) {
			Renderer.line(buffer, x, y, x + 1, y, w, c.r, c.g, c.b, c.a);
		}

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
			return this.symbols[turn];
		}catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * Advance the game to the next player and sends map updates
	 */
	public void nextPlayerTurn() {
		int len = this.symbols.length;

		Symbol symbol = getCurrentSymbol();
		sendPlayerTurnEvent(TurnEvent.TURN_END, symbol);

		turn = (turn + 1) % len;

		if (turn == 0) {
			sendPlayerTurnEvent(TurnEvent.TURN_CYCLE_END, symbol);
		}

		sendPlayerTurnEvent(TurnEvent.TURN_START, getCurrentSymbol());
	}

	/**
	 * Returns true if a tile is controlled by its owner, or false if it is disconnected
	 */
	public boolean canControl(int x, int y) {
		return control.canControl(x, y);
	}

	private void sendPlayerTurnEvent(TurnEvent event, Symbol symbol) {
		forEach((state, x, y) -> state.getTile().onPlayerTurnEvent(this, x, y, event, symbol));
		getEntities().forEach(entity -> entity.onPlayerTurnEvent(this, entity.getX(), entity.getY(), event, symbol));
		this.countries.forEach((key, value) -> value.onPlayerTurnEvent(this, event, symbol));
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

	public TileState[][] getTiles() {
		return tiles;
	}

	public Building getLinkedBuildingAt(int x, int y) {
		return buildings.get(new TilePos(x, y));
	}

	public void setLinkedBuildingAt(int x, int y, Building building) {
		buildings.put(new TilePos(x, y), building);
	}

	public TaskManager getManager() {
		return manager;
	}

	public TileState getTileState(TilePos pos) {
		return getTileState(pos.x, pos.y);
	}

	public void onOwnershipChanged() {
		ownershipDirty = true;
	}

	public WorldView getView() {
		return view;
	}

}
