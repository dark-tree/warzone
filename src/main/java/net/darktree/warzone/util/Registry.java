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
	 * Query registered element by its key
	 */
	public T getElement(String key) {
		return this.registry.get(key).value;
	}

	/**
	 * Query registered element by its identifier
	 */
	public T getElement(int identifier) {
		return this.list.get(identifier).value;
	}

	/**
	 * Get the identifier of an element in this registry
	 */
	public int identifierOf(T value) {
		return this.lookup.get(value).identifier;
	}

	/**
	 * Get the key of an element in this registry
	 */
	public String keyOf(T value) {
		return this.lookup.get(value).key;
	}

	public record Entry<T>(int identifier, String key, T value) {

	}

}
