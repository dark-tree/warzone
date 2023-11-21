package net.darktree.warzone.world.entity.building;

import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.country.Resource;
import net.darktree.warzone.country.Resources;
import net.darktree.warzone.country.storage.LimitedStorageStack;
import net.darktree.warzone.country.storage.Storage;
import net.darktree.warzone.country.storage.StorageNode;
import net.darktree.warzone.country.storage.StorageNodeSupplier;
import net.darktree.warzone.screen.ScreenStack;
import net.darktree.warzone.screen.WarehouseScreen;
import net.darktree.warzone.world.WorldAccess;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.entity.Entities;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WarehouseBuilding extends Building implements StorageNodeSupplier {

	private final LimitedStorageStack storage = new LimitedStorageStack();

	public WarehouseBuilding(WorldSnapshot world, int x, int y) {
		super(world, x, y, Entities.WAREHOUSE);
		storage.addResourceNode(Resources.MATERIALS, Storage.LARGE);
	}

	@Override
	public void onInteract(WorldAccess view, int x, int y, ClickEvent event) {
		ScreenStack.open(new WarehouseScreen(storage));
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
