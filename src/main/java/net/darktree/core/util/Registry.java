package net.darktree.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Registry<T> {

	private final Consumer<Entry<T>> listener;
	private final ArrayList<Entry<T>> list = new ArrayList<>();
	private final Map<String, Entry<T>> registry = new HashMap<>();
	private final Map<T, Entry<T>> lookup = new IdentityHashMap<>();

	public Registry(Consumer<Entry<T>> listener) {
		this.listener = listener;
	}

	public T register(String key, T value) {
		Entry<T> entry = new Entry<>(list.size(), key, value);

		if (this.lookup.get(value) != null || this.registry.get(key) != null) {
			throw new IllegalStateException("Registry already contains key '%s' or entry '%s'!".formatted(key, Integer.toHexString(value.hashCode())));
		}

		this.list.add(entry);
		this.registry.put(key, entry);
		this.lookup.put(value, entry);
		this.listener.accept(entry);

		return value;
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
