package net.darktree.game.state;

import net.darktree.game.nbt.NbtSerializable;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.IntTag;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

@SuppressWarnings("unchecked")
public class TileState implements NbtSerializable {

	public final static TileState DUMMY = TileState.create().alwaysBuild();

	private final HashMap<Property<?>, Integer> defaultState;
	private final HashMap<Property<?>, Integer> currentState;

	private TileState(HashMap<Property<?>, Integer> properties) {
		this.defaultState = properties;
		this.currentState = (HashMap<Property<?>, Integer>) properties.clone();
	}

	public static Builder create() {
		return new Builder();
	}

	public TileState set(Property<?> property, Object value) {
		this.currentState.put(property, property.indexOf(value));
		return this;
	}

	public <T> T get(Property<T> property) {
		return property.values()[this.currentState.get(property)];
	}

	public TileState reset() {
		currentState.putAll(defaultState);
		return this;
	}

	@Override
	public void toNbt(@NotNull CompoundTag tag) {
		currentState.forEach((property, value) -> {
			tag.putInt(property.name(), value);
		});
	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {
		defaultState.forEach((property, value) -> {
			IntTag val = tag.getIntTag(property.name());
			currentState.put(property, val == null ? value : val.asInt());
		});
	}

	@SuppressWarnings("unchecked")
	public static class Builder {
		private final HashMap<Property<?>, Integer> properties = new HashMap<>();

		public Builder add(Property<?> property, Object value) {
			this.properties.put(property, property.indexOf(value));
			return this;
		}

		private TileState alwaysBuild() {
			return new TileState((HashMap<Property<?>, Integer>) this.properties.clone());
		}

		public TileState build() {
			return (this.properties.size() == 0) ? DUMMY : alwaysBuild();
		}
	}

}
