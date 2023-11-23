package net.darktree.warzone.world;

import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.util.NbtSerializable;
import net.darktree.warzone.util.Util;
import net.darktree.warzone.util.math.Vec2i;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.terrain.BorderFinder;
import net.darktree.warzone.world.terrain.ControlFinder;
import net.darktree.warzone.world.terrain.EnclaveFinder;
import net.darktree.warzone.world.terrain.PrincipalityFinder;
import net.darktree.warzone.world.tile.TileState;
import net.darktree.warzone.world.tile.tiles.Tiles;
import net.darktree.warzone.world.tile.variant.TileVariant;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorldSnapshot implements NbtSerializable, WorldEntityView {

	private final WorldInfo info;
	private final WorldAccess access;
	private final Frame frame;

	private final List<Entity> entities = new ArrayList<>();
	private final HashMap<Symbol, Country> countries = new HashMap<>();
	private final TileState[][] tiles;

	private BorderFinder border;
	private ControlFinder control;
	private PrincipalityFinder principality;

	public WorldSnapshot(WorldAccess access, Frame frame) {
		this.info = access.getInfo();
		this.access = access;
		this.frame = frame;
		this.tiles = new TileState[info.width][info.height];

		TileVariant variant = Tiles.EMPTY.getDefaultVariant();

		for (int x = 0; x < info.width; x++) {
			for (int y = 0; y < info.height; y++) {
				this.tiles[x][y] = new TileState(this, variant, Symbol.NONE);
			}
		}

//		pushUpdateBits(Update.ALL); TODO this fails as the frame is null, find a better way to do this
	}

	/**
	 * Creates a copy of this world snapshot
	 */
	public WorldSnapshot copy(Frame parent) {
		WorldSnapshot copy = new WorldSnapshot(access, parent);

		for (int x = 0; x < info.width; x++) {
			for (int y = 0; y < info.height; y++) {
				copy.tiles[x][y] = getTileState(x, y).copy(copy);
			}
		}

		getEntities().forEach(entity -> {
			copy.addEntity(entity.copy(copy));
		});

		for (Country country : countries.values()) {
			copy.countries.put(country.symbol, country.copy(copy));
		}

		copy.pushUpdateBits(Update.ALL);
		return copy;
	}

	/**
	 * Checks if the given position falls withing the confines of the map,
	 * shorthand for <pre>getInfo().isPositionValid(x, y)}</pre>
	 */
	public boolean isPositionValid(int x, int y) {
		return info.isPositionValid(x, y);
	}

	/**
	 * Returns the instance of world access this world belongs to, see {@link WorldAccess}.
	 */
	public WorldAccess getAccess() {
		return access;
	}

	/**
	 * Returns the ledger that this world belongs to, see {@link WorldLedger}.
	 */
	public WorldLedger getLedger() {
		return getAccess().getLedger();
	}

	/**
	 * Returns the world constant, see {@link WorldInfo}.
	 */
	public WorldInfo getInfo() {
		return info;
	}

	/**
	 * Returns a up-to-date border finder for this world.
	 * TODO: reevaluate this, maybe have a isBorderTile/getBorderTiles method?
	 */
	public BorderFinder getBorder() {
		return border;
	}

	/**
	 * Forces the specified updates to be applied, see {@link Update}.
	 * Don't use this method after setting a surface or owner using
	 * setOwner/setVariant from {@link TileState} - those method do it for you.
	 */
	public void pushUpdateBits(@Update.Flags int flags) {
		access.pushRenderBits(flags);
	}

	// FIXME
	private void sendPlayerTurnEvent(TurnEvent event, Symbol symbol) {
		getEntities().forEach(entity -> entity.onPlayerTurnEvent(event, symbol));
		this.countries.forEach((key, value) -> value.onPlayerTurnEvent(event, symbol));
	}

	void update(@Update.Flags int flags) {
		Util.consumeIf(entities, Entity::isRemoved, WorldComponent::onRemoved);

		if ((flags & Update.CONTROL) != 0) {
			this.control = new ControlFinder(this);
		}

		if ((flags & Update.BORDER) != 0) {
			this.border = new BorderFinder(this);
			this.principality = new PrincipalityFinder(border);

			EnclaveFinder finder = new EnclaveFinder(this, this.control);

			finder.getEnclaves().forEach(enclave -> {
				Symbol symbol = enclave.encircled();

				if (symbol != null && symbol != Symbol.NONE) {
					enclave.forEachTile(pos -> {
						getTileState(pos).setOwner(symbol, true);
					});
				}
			});
		}
	}

	/**
	 * Returns the {@link TileState} at the given position. The TileState object is
	 * mutable, so you can use it to modify the world.
	 *
	 * @throws IndexOutOfBoundsException if the given position is not within the bounds of this world
	 */
	public TileState getTileState(int x, int y) {
		if (isPositionValid(x, y)) {
			return this.tiles[x][y];
		}

		throw new IndexOutOfBoundsException("Position (" + x + ", " + y + ") is out of world bounds!");
	}

	/**
	 * See {@link WorldSnapshot#getTileState(int, int)}
	 *
	 * @throws IndexOutOfBoundsException if the given position is not within the bounds of this world
	 */
	public TileState getTileState(Vec2i pos) {
		return getTileState(pos.x, pos.y);
	}

	@Nullable
	@Override
	public Entity getEntity(int x, int y) {
		return getTileState(x, y).getEntity();
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public Country getCountry(Symbol symbol) {
		return countries.get(symbol);
	}

	/**
	 * Get the symbol of currently active player,
	 * returns null if there was an issue.
	 */
	public Symbol getCurrentSymbol() {
		return frame.getSymbol();
	}

	public Symbol getPrincipality(int x, int y) {
		return principality.getPrincipality(x, y);
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

	public Country defineCountry(Symbol symbol) {
		Country country = new Country(symbol, this);
		countries.put(symbol, country);
		return country;
	}

	@Override
	public void toNbt(@NotNull CompoundTag nbt) {
		ListTag<CompoundTag> entitiesTagList = new ListTag<>(CompoundTag.class);
		ListTag<CompoundTag> tilesTagList = new ListTag<>(CompoundTag.class);
		ListTag<CompoundTag> countriesTagList = new ListTag<>(CompoundTag.class);

		for (Entity entity : this.entities) {
			CompoundTag entityTag = new CompoundTag();
			entity.toNbt(entityTag);
			entitiesTagList.add(entityTag);
		}

		for (short x = 0; x < info.width; x ++) {
			for (short y = 0; y < info.height; y ++) {
				CompoundTag tileTag = new CompoundTag();

				// TODO: consider if the state shouldn't just know its position
				tileTag.putShort("x", x);
				tileTag.putShort("y", y);

				this.tiles[x][y].toNbt(tileTag);
				tilesTagList.add(tileTag);
			}
		}

		for (Country country : countries.values()) {
			CompoundTag countryTag = new CompoundTag();

			country.toNbt(countryTag);
			countriesTagList.add(countryTag);
		}

		nbt.put("tiles", tilesTagList);
		nbt.put("entities", entitiesTagList);
		nbt.put("countries", countriesTagList);
	}

	public void fromNbt(@NotNull CompoundTag nbt) {
		ListTag<CompoundTag> tilesTagList = nbt.getListTag("tiles").asCompoundTagList();
		ListTag<CompoundTag> entitiesTagList = nbt.getListTag("entities").asCompoundTagList();
		ListTag<CompoundTag> countriesTagList = nbt.getListTag("countries").asCompoundTagList();

		for (CompoundTag tileTag : tilesTagList) {
			short x = tileTag.getShort("x");
			short y = tileTag.getShort("y");

			tiles[x][y].fromNbt(tileTag);
		}

		// load countries before entities
		for (CompoundTag countryTag : countriesTagList) {
			Symbol symbol = Symbol.values()[countryTag.getByte("symbol")];

			defineCountry(symbol).fromNbt(countryTag);
		}

		for (CompoundTag entityTag : entitiesTagList) {
			Entity entity = Entity.load(this, entityTag);
			entity.onLoaded();

			addEntity(entity);
		}
	}

}
