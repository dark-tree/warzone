//package net.darktree.game;
//
//import net.darktree.game.nbt.NbtSerializable;
//import net.querz.nbt.tag.CompoundTag;
//import net.querz.nbt.tag.IntTag;
//import net.querz.nbt.tag.Tag;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class TilePallet implements NbtSerializable {
//
//	private final HashMap<Tile.Type, Integer> pallet = new HashMap<>();
//	private final HashMap<Integer, Tile.Type> lookup = new HashMap<>();
//
//	@Override
//	public void toNbt(@NotNull CompoundTag tag) {
//		for (Map.Entry<Tile.Type, Integer> entry : this.pallet.entrySet()) {
//			tag.putInt(Registries.TILES.getKey(entry.getKey()), entry.getValue());
//		}
//	}
//
//	@Override
//	public void fromNbt(@NotNull CompoundTag tag) {
//		for (Map.Entry<String, Tag<?>> entry : tag.entrySet()) {
//			insert(Registries.TILES.getElement(entry.getKey()), ((IntTag) entry.getValue()).asInt());
//		}
//	}
//
//	private int insert(Tile.Type type, int id) {
//		this.pallet.put(type, id);
//		this.lookup.put(id, type);
//		return id;
//	}
//
//	public int add(Tile tile) {
//		if (!this.pallet.containsKey(tile.type)) {
//			return insert(tile.type, this.pallet.size());
//		}
//
//		return get(tile);
//	}
//
//	public Tile get(int id, @Nullable CompoundTag tag, World world, int x, int y) {
//		Tile.Type type = this.lookup.get(id);
//		return type.create(type, world, tag, x, y);
//	}
//
//	public int get(Tile tile) {
//		return this.pallet.get(tile.type);
//	}
//
//}
//
