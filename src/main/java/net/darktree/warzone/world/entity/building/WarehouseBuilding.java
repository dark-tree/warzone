package net.darktree.warzone.world.entity.building;

import net.darktree.warzone.country.Resource;
import net.darktree.warzone.country.Resources;
import net.darktree.warzone.country.storage.MultiStorageNode;
import net.darktree.warzone.country.storage.StorageNode;
import net.darktree.warzone.country.storage.StorageNodeSupplier;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.tile.tiles.Tiles;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WarehouseBuilding extends Building implements StorageNodeSupplier {

	private final MultiStorageNode storage = new MultiStorageNode();

	public WarehouseBuilding(World world, int x, int y) {
		super(world, x, y, Tiles.WAREHOUSE);
		storage.addResourceNode(Resources.MATERIALS, 100);
	}

	@Override
	public void appendStorageNodes(Resource resource, List<StorageNode> list) {
		storage.appendStorageNodes(resource, list);
	}

	@Override
	public void toNbt(@NotNull CompoundTag nbt) {
		super.toNbt(nbt);
		storage.toNbt(nbt);
	}

	@Override
	public void fromNbt(@NotNull CompoundTag nbt) {
		super.fromNbt(nbt);
		storage.fromNbt(nbt);
	}

}
