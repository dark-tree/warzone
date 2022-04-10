package net.darktree.lt2d.world.state;

import net.darktree.lt2d.world.Tile;
import net.darktree.lt2d.world.state.property.Property;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.IntTag;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class TileState {

	private final Tile tile;
	private final PropertyTree tree;
	private final HashMap<Property<?>, Object> state;

	TileState(Tile tile, HashMap<Property<?>, Object> state, PropertyTree tree) {
		this.tile = tile;
		this.tree = tree;
		this.state = state;
	}

	public static TileState createOf(Tile tile, Property<?>... properties) {
		return new PropertyTree(tile, properties).getDefault();
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Property<T> property) {
		return (T) this.state.get(property);
	}

	public <T> TileState with(Property<T> property, T value) {
		return this.tree.get(this.state, property, value);
	}

	public Tile getTile() {
		return tile;
	}

	public void toNbt(@NotNull CompoundTag tag) {
		this.state.forEach((property, value) -> {
			tag.putInt(property.name(), property.indexOf(value));
		});
	}

	public TileState fromNbt(@NotNull CompoundTag tag) {
		HashMap<Property<?>, Object> config = new HashMap<>();

		this.state.forEach((property, value) -> {
			IntTag val = tag.getIntTag(property.name());
			config.put(property, val == null ? value : property.values()[val.asInt()]);
		});

		return this.tree.get(config, null, null);
	}
}
