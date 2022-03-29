package net.darktree.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class Registry<T> {

	private final Consumer<Entry<T>> listener;
	private final ArrayList<Entry<T>> list = new ArrayList<>();
	private final HashMap<String, Entry<T>> registry = new HashMap<>();
	private final HashMap<T, Entry<T>> lookup = new HashMap<>();

	public Registry(Consumer<Entry<T>> listener) {
		this.listener = listener;
	}

	public T register(String key, T value) {
		Entry<T> entry = new Entry<>(list.size(), key, value);

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

	public record Entry<T>(int identifier, String key, T value) {

	}

}
