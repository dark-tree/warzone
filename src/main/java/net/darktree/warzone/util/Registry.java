package net.darktree.warzone.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class Registry<T extends ElementType<T>> {

	private final ArrayList<Entry<T>> list = new ArrayList<>();
	private final Map<String, Entry<T>> registry = new HashMap<>();
	private final Map<T, Entry<T>> lookup = new IdentityHashMap<>();

	public <E extends T> E register(String key, E value) {
		Entry<T> entry = new Entry<>(list.size(), key, value);

		if (this.lookup.get(value) != null || this.registry.get(key) != null) {
			throw new IllegalStateException("Registry already contains key '%s' or entry '%s'!".formatted(key, Integer.toHexString(value.hashCode())));
		}

		this.list.add(entry);
		this.registry.put(key, entry);
		this.lookup.put(value, entry);
		entry.value.onRegister(key);

		return value;
	}

	/**
	 * Create a map from the registry where keys are the registry values
	 */
	public <R> Map<T, R> map(Map<T, R> map, Function<T, R> mapper) {
		iterate(entry -> map.put(entry, mapper.apply(entry)));
		return map;
	}

	/**
	 * Run the given consumer for all entries in the registry
	 */
	public void iterate(Consumer<T> consumer) {
		list.forEach(entry -> consumer.accept(entry.value));
	}

	/**
	 * Get the number of entries in this registry
	 */
	public int count() {
		return lookup.size();
	}

	/**
	 * Query entry by element's string key
	 */
	public Entry<T> byKey(String key) {
		return this.registry.get(key);
	}

	/**
	 * Query entry by element's integer id
	 */
	public Entry<T> byId(int index) {
		return this.list.get(index);
	}

	/**
	 * Query entry by element's value
	 */
	public Entry<T> byValue(T value) {
		return this.lookup.get(value);
	}

	public record Entry<T>(int id, String key, T value) {

	}

}
