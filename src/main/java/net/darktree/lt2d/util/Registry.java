package net.darktree.lt2d.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class Registry<T> {

	private final Consumer<Entry<T>> listener;
	private final Consumer<Registry<T>> finalizer;
	private final ArrayList<Entry<T>> list = new ArrayList<>();
	private final HashMap<String, Entry<T>> registry = new HashMap<>();
	private final HashMap<T, Entry<T>> lookup = new HashMap<>();

	public Registry(Consumer<Entry<T>> listener, Consumer<Registry<T>> finalizer) {
		this.listener = listener;
		this.finalizer = finalizer;
	}

	public T register(String key, T value) {
		Entry<T> entry = new Entry<>(list.size(), key, value);

		if( this.lookup.get(value) != null || this.registry.get(key) != null ) {
			throw new IllegalStateException("Registry already contains key '%s' or entry '%s'!".formatted(key, value.hashCode()));
		}

		this.list.add(entry);
		this.registry.put(key, entry);
		this.lookup.put(value, entry);
		this.listener.accept(entry);

		return value;
	}

	public T getElement(String key) {
		return this.registry.get(key).value;
	}

	public T getElement(int key) {
		return this.list.get(key).value;
	}

	public int getIdentifier(String key) {
		return this.registry.get(key).identifier;
	}

	public int getIdentifier(T value) {
		return this.lookup.get(value).identifier;
	}

	public String getKey(T value) {
		return this.lookup.get(value).key;
	}

	public void freeze() {
		this.finalizer.accept(this);
	}

	public record Entry<T>(int identifier, String key, T value) {

	}

}
