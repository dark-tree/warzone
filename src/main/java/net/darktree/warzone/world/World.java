package net.darktree.warzone.world;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.render.WorldBuffers;
import net.darktree.warzone.client.render.vertex.Renderer;
import net.darktree.warzone.client.render.vertex.VertexBuffer;
import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.event.TurnEvent;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.Util;
import net.darktree.warzone.world.action.manager.ActionManager;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.overlay.Overlay;
import net.darktree.warzone.world.terrain.ControlFinder;
import net.darktree.warzone.world.terrain.EnclaveFinder;
import net.darktree.warzone.world.tile.TilePos;
import net.darktree.warzone.world.tile.TileState;
import net.darktree.warzone.world.tile.variant.TileVariant;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class World implements WorldEntityView {

	final public int width, height;

	final private TileState[][] tiles;
	final private List<Entity> entities = new ArrayList<>();
	final private HashMap<Symbol, Country> countries = new HashMap<>();

	private ControlFinder control;
	private Symbol[] symbols = new Symbol[]{};
	final private ActionManager manager = new ActionManager.Host(this);
	private Overlay overlay = null;
	private boolean ownershipDirty = true, redrawSurface = true, redrawBuildings = true;
	private int turn;

	private final WorldView view;

	public World(int width, int height) {
		this.width = width;
		this.height = height;
		this.tiles = new TileState[width][height];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				this.tiles[x][y] = new TileState(null, Symbol.NONE);
			}
		}

		this.view = new WorldView(width, height);
		this.control = new ControlFinder(this);
	}

	public void toNbt(@NotNull CompoundTag tag) {
		CompoundTag tilesTag = new CompoundTag();

		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				CompoundTag tileTag = new CompoundTag();
				this.tiles[x][y].toNbt(tileTag);
				tilesTag.put(x + " " + y, tileTag);
			}
		}

		ListTag<CompoundTag> entities = new ListTag<>(CompoundTag.class);

		for (Entity entity : this.entities) {
			CompoundTag entityTag = new CompoundTag();
			entity.toNbt(entityTag);
			entities.add(entityTag);
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
		tag.put("entities", entities);
		tag.put("countries", countriesTag);
	}

	public static void load(CompoundTag tag) {
		CompoundTag tilesTag = tag.getCompoundTag("tiles");
		ListTag<?> entities = tag.getListTag("entities");
		CompoundTag countriesTag = tag.getCompoundTag("countries");

		World world = new World(tag.getInt("width"), tag.getInt("height"));
		world.turn = tag.getByte("turn");

		WorldHolder.setWorld(world);

		List<Symbol> symbols = new ArrayList<>();

		for (Symbol symbol : Symbol.values()) {
			CompoundTag countryTag = countriesTag.getCompoundTag(symbol.name());

			if (countryTag != null) {
				world.defineCountry(symbol).fromNbt(countryTag);
				symbols.add(symbol);
			}
		}

		world.symbols = symbols.toArray(new Symbol[]{});

		for (int x = 0; x < world.width; x++) {
			for (int y = 0; y < world.height; y++) {
				world.tiles[x][y].load(world, x, y, tilesTag);
			}
		}

		entities.forEach(entry -> {
			Entity entity = Entity.load(world, (CompoundTag) entry);
			entity.onLoaded();
			world.addEntity(entity);
		});

		Logger.info("World loaded!");
	}

	public void loadTiles(Function<TilePos, TileVariant> generator) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				setTileVariant(x, y, generator.apply(new TilePos(x, y)));
			}
		}
	}

	public boolean isPositionValid(int x, int y) {
		return x >= 0 && y >= 0 && x < width && y < height;
	}

	@Override
	public List<Entity> getEntities() {
		return entities;
	}

	@Override
	public Entity getEntity(int x, int y) {
		return getTileState(x, y).getEntity();
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

	public void draw(WorldBuffers buffers) {
		Util.consumeIf(entities, Entity::isRemoved, WorldComponent::onRemoved);

		if (ownershipDirty) {
			ownershipDirty = false;

			this.control = new ControlFinder(this);
			EnclaveFinder finder = new EnclaveFinder(this, this.control);

			finder.getEnclaves().forEach(enclave -> {
				Symbol symbol = enclave.encircled();

				if (symbol != null && symbol != Symbol.NONE) {
					enclave.forEachTile(pos -> {
						setTileOwner(pos.x, pos.y, symbol);
					});
				}
			});
		}

		if (redrawSurface) {
			buffers.getSurface().clear();

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					TileState state = this.tiles[x][y];
					state.getTile().draw(x, y, buffers.getSurface());

					drawBorders(buffers.getSurface(), x, y);
				}
			}

			Logger.info("Surface redrawn, using " + buffers.getSurface().count() + " vertices");
		}

		if (redrawBuildings) {
			buffers.getBuilding().clear();
		}

		this.entities.forEach(entity -> entity.draw(buffers, redrawBuildings));

		if (overlay != null) {
			VertexBuffer buffer = buffers.getOverlay();

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					Renderer.overlay(buffer, x, y, overlay.getColor(this, x, y, getTileState(x, y)));
				}
			}
		}

		redrawSurface = false;
		redrawBuildings = false;
	}

	private void drawBorders(VertexBuffer buffer, int x, int y) {
		Symbol self = tiles[x][y].getOwner();
		float w = 0.03f;

		if (x != 0 && tiles[x - 1][y].getOwner() != self) {
			Renderer.line(buffer, x, y, x, y + 1, w, Colors.BORDER);
		}

		if (y != 0 && tiles[x][y - 1].getOwner() != self) {
			Renderer.line(buffer, x, y, x + 1, y, w, Colors.BORDER);
		}

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
		manager.clear(symbol);

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

	/**
	 * Returns true if a tile is controlled by the given symbol, or false if it is not
	 */
	public boolean canControl(int x, int y, Symbol symbol) {
		return control.canControl(x, y) && getTileState(x, y).getOwner() == symbol;
	}

	private void sendPlayerTurnEvent(TurnEvent event, Symbol symbol) {
		getEntities().forEach(entity -> entity.onPlayerTurnEvent(event, symbol));
		this.countries.forEach((key, value) -> value.onPlayerTurnEvent(this, event, symbol));
	}

	public Country getCountry(Symbol symbol) {
		return countries.get(symbol);
	}

	public Country getCountry(int x, int y) {
		return countries.get(getTileState(x, y).getOwner());
	}

	public void setOverlay(Overlay overlay) {
		this.overlay = overlay;
	}

	public TileState[][] getTiles() {
		return tiles;
	}

	public ActionManager getManager() {
		return manager;
	}

	public TileState getTileState(TilePos pos) {
		return getTileState(pos.x, pos.y);
	}

	public void onOwnershipChanged() {
		ownershipDirty = true;
		redrawSurface = true;
	}

	public void onTileChanged() {
		redrawSurface = true;
	}

	public void onBuildingChanged() {
		redrawBuildings = true;
	}

	public WorldView getView() {
		return view;
	}

}
