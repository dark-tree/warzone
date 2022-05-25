package net.darktree.core.world.tile.variant;

import net.darktree.core.world.tile.Tile;
import net.darktree.core.world.tile.variant.property.Property;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.stream.Collectors;

public class TileVariant {

	private final Tile tile;
	private final PropertyTree tree;
	private final HashMap<Property<?>, Object> state;
	private final int index;

	TileVariant(Tile tile, HashMap<Property<?>, Object> state, PropertyTree tree, int index) {
		this.tile = tile;
		this.tree = tree;
		this.state = state;
		this.index = index;
	}

	public static TileVariant createOf(Tile tile, Property<?>... properties) {
		return new PropertyTree(tile, properties).getDefault();
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Property<T> property) {
		return (T) this.state.get(property);
	}

	public <T> TileVariant with(Property<T> property, T value) {
		return this.tree.get(this.state, property, value);
	}

	public Tile getTile() {
		return tile;
	}

	public void toNbt(@NotNull CompoundTag tag) {
		if (this.index != 0) tag.putInt("state", this.index);
	}

	public TileVariant fromNbt(@NotNull CompoundTag tag) {
		return this.tree.getStates().get(tag.getInt("state"));
	}

	@Override
	public String toString() {
		return this.state.entrySet().stream().map(entry -> entry.getKey().name() + "=" + entry.getValue().toString()).collect(Collectors.joining(","));
	}

}
