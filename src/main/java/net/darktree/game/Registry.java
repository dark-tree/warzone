package net.darktree.game;

import net.darktree.game.tiles.EmptyTile;

import java.util.ArrayList;
import java.util.HashMap;

public class Registry {

	private static final ArrayList<Tile.Factory> factories = new ArrayList<>();
	private static final HashMap<Class<?>, Integer> ids = new HashMap<>();

	public static <T extends Tile> void register(String name, Class<T> clazz, Tile.Factory factory) {
		factories.add(factory);
		ids.put(clazz, factories.size() - 1);
	}

	public static Tile.Factory getFactory(int id) {
		return factories.get(id);
	}

	public static int getId(Class<?> clazz) {
		return ids.get(clazz);
	}

	// TODO move this somewhere else
	static {
		register("empty", EmptyTile.class, EmptyTile::new);
	}

}
