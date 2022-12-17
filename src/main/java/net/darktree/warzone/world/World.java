package net.darktree.warzone.world;

import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.event.TurnEvent;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.NbtSerializable;
import net.darktree.warzone.util.Util;
import net.darktree.warzone.world.action.manager.ActionManager;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.terrain.BonusFinder;
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

public class World implements WorldEntityView, NbtSerializable {

	private final List<Entity> entities = new ArrayList<>();
	private final HashMap<Symbol, Country> countries = new HashMap<>();

	private int width, height;
	private TileState[][] tiles;
	private Symbol[] symbols = new Symbol[]{};
	private boolean ownershipDirty = true;
	private int turn;
	private WorldRenderer renderer;
	private ControlFinder control;

	public ActionManager manager = new ActionManager(this);

	public World(int width, int height) {
		resize(width, height);
	}

	private void resize(int width, int height) {
		this.width = width;
		this.height = height;
		this.tiles = new TileState[width][height];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				this.tiles[x][y] = new TileState(null, Symbol.NONE);
			}
		}

		this.ownershipDirty = true;
		this.renderer = new WorldRenderer(this);
		this.control = new ControlFinder(this);

		// FIXME: ugly hack to force buffer reload
		WorldHolder.setWorld(this);
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

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {
		CompoundTag tilesTag = tag.getCompoundTag("tiles");
		ListTag<?> entities = tag.getListTag("entities");
		CompoundTag countriesTag = tag.getCompoundTag("countries");

		resize(tag.getInt("width"), tag.getInt("height"));
		turn = tag.getByte("turn");

		List<Symbol> symbols = new ArrayList<>();

		for (Symbol symbol : Symbol.values()) {
			CompoundTag countryTag = countriesTag.getCompoundTag(symbol.name());

			if (countryTag != null) {
				defineCountry(symbol).fromNbt(countryTag);
				symbols.add(symbol);
			}
		}

		this.symbols = symbols.toArray(new Symbol[]{});

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				tiles[x][y].load(this, x, y, tilesTag);
			}
		}

		entities.forEach(entry -> {
			Entity entity = Entity.load(this, (CompoundTag) entry);
			entity.onLoaded();
			addEntity(entity);
		});

		Logger.info("World loaded!");
	}

	@Deprecated
	public static void load(CompoundTag tag) {
		World world = new World(0, 0);
		WorldHolder.setWorld(world);
		world.fromNbt(tag);
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
		getTileState(x, y).setVariant(this, variant);
	}

	/**
	 * Set tile owner at the given position
	 *
	 *  @throws IndexOutOfBoundsException if the given position is invalid
	 */
	public void setTileOwner(int x, int y, Symbol owner) {
		getTileState(x, y).setOwner(this, owner, true);
	}

	public void update() {
		Util.consumeIf(entities, Entity::isRemoved, WorldComponent::onRemoved);

		if (ownershipDirty) {
			ownershipDirty = false;

			this.control = new ControlFinder(this);
			EnclaveFinder finder = new EnclaveFinder(this, this.control);

			finder.getEnclaves().forEach(enclave -> {
				Symbol symbol = enclave.encircled();

				if (symbol != null && symbol != Symbol.NONE) {
					enclave.forEachTile(pos -> setTileOwner(pos.x, pos.y, symbol));
				}
			});
		}
	}

	@Deprecated
	public void markOverlayDirty() {
		renderer.markOverlayDirty();
	}

	public Country defineCountry(Symbol symbol) {
		Country country = new Country(symbol);
		countries.put(symbol, country);
		return country;
	}

	/**
	 * Get the symbol of currently active player,
	 * returns null if there was an issue.
	 */
	public Symbol getCurrentSymbol() {
		try {
			return this.symbols[turn];
		}catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	public boolean isActiveSymbol() {
		return self == getCurrentSymbol();
	}

	public Symbol getActiveSymbol() {
		return isActiveSymbol() ? self : null;
	}

	public Symbol self = Symbol.CROSS;

	public Symbol getSelf() {
		return self;
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
		return canControl(x, y) && getTileState(x, y).getOwner() == symbol;
	}

	/**
	 * Grants bonus tiles (if there are any) to the given player
	 */
	public boolean grantBonusTiles(Symbol symbol) {
		return new BonusFinder(this, symbol).grant() > 0;
	}

	private void sendPlayerTurnEvent(TurnEvent event, Symbol symbol) {
		getEntities().forEach(entity -> entity.onPlayerTurnEvent(event, symbol));
		this.countries.forEach((key, value) -> value.onPlayerTurnEvent(this, event, symbol));
	}

	public Country getCountry(Symbol symbol) {
		return countries.get(symbol);
	}

	public Country getCountry(int x, int y) {
		return getCountry(getTileState(x, y).getOwner());
	}

	public ActionManager getManager() {
		return manager;
	}

	public TileState getTileState(TilePos pos) {
		return getTileState(pos.x, pos.y);
	}

	public void onOwnershipChanged() {
		ownershipDirty = true;
		renderer.markSurfaceDirty();
	}

	@Deprecated
	public void onTileChanged() {
		renderer.markSurfaceDirty();
	}

	@Deprecated
	public void onBuildingChanged() {
		renderer.markBuildingsDirty();
	}

	public WorldRenderer getView() {
		return renderer;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
