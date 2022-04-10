package net.darktree.lt2d.world.state;

import net.darktree.lt2d.world.Tile;
import net.darktree.lt2d.world.state.property.Property;

import java.util.HashMap;

public class PropertyTree {

	private final Property<?>[] keys;
	private final Object tree;
	private final int size;

	public PropertyTree(Tile tile, Property<?>... properties) {
		this.size = properties.length;
		this.tree = properties.length > 0 ? getDefaultTree(tile, properties, 0, new HashMap<>()) : new TileState(tile, new HashMap<>(), this);
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
				map.put(value, new TileState(tile, current, this));
			}

		}

		return map;
	}

	TileState get(HashMap<Property<?>, Object> config, Property<?> key, Object value) {
		Object current = this.tree;

		for (int i = 0; i < this.size; i ++) {
			current = (keys[i] == key ? ((HashMap<?, ?>) current).get(value) : ((HashMap<?, ?>) current).get(config.get(keys[i])));
		}

		return (TileState) current;
	}

	TileState getDefault() {
		HashMap<Property<?>, Object> config = new HashMap<>();

		for (Property<?> property : this.keys) {
			config.put(property, property.getDefault());
		}

		return get(config, null, null);
	}

	public Property<?>[] getKeys() {
		return this.keys;
	}

}
