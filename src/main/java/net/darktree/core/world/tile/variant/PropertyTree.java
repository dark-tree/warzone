package net.darktree.core.world.tile.variant;

import net.darktree.core.world.tile.Tile;
import net.darktree.core.world.tile.variant.property.Property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PropertyTree {

	private final List<TileVariant> states = new ArrayList<>();
	private final Property<?>[] keys;
	private final Object tree;
	private final int size;

	public PropertyTree(Tile tile, Property<?>... properties) {
		this.size = properties.length;
		this.tree = properties.length > 0 ? getDefaultTree(tile, properties, 0, new HashMap<>()) : produceState(tile, new HashMap<>());
		this.keys = properties;
	}

	@SuppressWarnings("unchecked")
	private HashMap<Object, Object> getDefaultTree(Tile tile, Property<?>[] properties, int depth, HashMap<Property<?>, Object> state) {
		HashMap<Object, Object> map = new HashMap<>();
		int next = depth + 1;

		for (Object value : properties[depth].values()) {

			HashMap<Property<?>, Object> current = (HashMap<Property<?>, Object>) state.clone();
			current.put(properties[depth], value);

			if (next < size) {
				map.put(value, getDefaultTree(tile, properties, next, current));
			} else {
				map.put(value, produceState(tile, current));
			}

		}

		return map;
	}

	private TileVariant produceState(Tile tile, HashMap<Property<?>, Object> config) {
		TileVariant state = new TileVariant(tile, config, this);
		states.add(state);
		return state;
	}

	TileVariant get(HashMap<Property<?>, Object> config, Property<?> key, Object value) {
		Object current = this.tree;

		for (int i = 0; i < this.size; i ++) {
			current = (keys[i] == key ? ((HashMap<?, ?>) current).get(value) : ((HashMap<?, ?>) current).get(config.get(keys[i])));
		}

		return (TileVariant) current;
	}

	TileVariant getDefault() {
		HashMap<Property<?>, Object> config = new HashMap<>();

		for (Property<?> property : this.keys) {
			config.put(property, property.getDefault());
		}

		return get(config, null, null);
	}

	public Property<?>[] getKeys() {
		return this.keys;
	}

	public List<TileVariant> getStates() {
		return states;
	}
}
