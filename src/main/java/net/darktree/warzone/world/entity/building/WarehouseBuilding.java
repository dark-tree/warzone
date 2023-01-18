package net.darktree.warzone.world.entity.building;

import net.darktree.warzone.country.Resource;
import net.darktree.warzone.country.Resources;
import net.darktree.warzone.country.storage.LimitedStorageStack;
import net.darktree.warzone.country.storage.Storage;
import net.darktree.warzone.country.storage.StorageNode;
import net.darktree.warzone.country.storage.StorageNodeSupplier;
import net.darktree.warzone.event.ClickEvent;
import net.darktree.warzone.screen.ScreenStack;
import net.darktree.warzone.screen.WarehouseScreen;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.tile.tiles.Tiles;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WarehouseBuilding extends Building implements StorageNodeSupplier {

	private final LimitedStorageStack storage = new LimitedStorageStack();

	public WarehouseBuilding(World world, int x, int y) {
		super(world, x, y, Tiles.WAREHOUSE);
		storage.addResourceNode(Resources.MATERIALS, Storage.LARGE);
	}

	@Override
	public void onInteract(World world, int x, int y, ClickEvent event) {
		if (event.isPressed()) {
			ScreenStack.open(new WarehouseScreen(storage));
		}
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