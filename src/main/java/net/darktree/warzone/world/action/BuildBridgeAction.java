package net.darktree.warzone.world.action;

import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.country.Resources;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.util.Direction;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.action.ledger.Action;
import net.darktree.warzone.world.entity.Entities;
import net.darktree.warzone.world.entity.building.BridgeStructure;
import net.darktree.warzone.world.tile.TilePos;
import net.querz.nbt.tag.CompoundTag;

import java.util.ArrayList;
import java.util.List;

public final class BuildBridgeAction extends Action {

	private final int x, y;
	private final Direction facing;

	public BuildBridgeAction(int x, int y, Direction facing) {
		super(Actions.BUILD_BRIDGE);
		this.x = x;
		this.y = y;
		this.facing = facing;
	}

	public BuildBridgeAction(CompoundTag nbt) {
		this(nbt.getInt("x"), nbt.getInt("y"), Direction.values()[nbt.getByte("facing")]);
	}

	@Override
	public void toNbt(CompoundTag nbt) {
		super.toNbt(nbt);
		nbt.putInt("x", x);
		nbt.putInt("y", y);
		nbt.putByte("facing", (byte) facing.ordinal());
	}

	@Override
	public boolean redo(WorldSnapshot world, boolean animate) {
		Symbol symbol = world.getCurrentSymbol();
		BridgePlacer bridge = BridgePlacer.create(world, x, y, facing, false);

		if (bridge == null || !bridge.isFullyValid(symbol)) {
			return false;
		}

		List<BridgeStructure> parts = new ArrayList<>();

		for (TilePos pos : bridge.getTiles()) {
			parts.add((BridgeStructure) Entities.BRIDGE.create(world, pos.x, pos.y));
		}

		bridge.configure(parts);

		for (BridgeStructure part : parts) {
			world.addEntity(part);
		}

		world.getCountry(symbol).getResource(Resources.MATERIALS).add(-bridge.getCost());
		Sounds.STAMP.play(x, y);
		return true;
	}

}
