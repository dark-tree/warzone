package net.darktree.warzone.world.action;

import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.util.Direction;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.action.manager.Action;
import net.darktree.warzone.world.entity.Entities;
import net.darktree.warzone.world.entity.building.BridgeStructure;
import net.darktree.warzone.world.tile.TilePos;
import net.querz.nbt.tag.CompoundTag;

import java.util.ArrayList;
import java.util.List;

public class BuildBridgeAction extends Action {

	private final int x, y;
	private final Direction facing;
	private final List<BridgeStructure> parts = new ArrayList<>();

	private BridgePlacer bridge = null;

	public BuildBridgeAction(World world, int x, int y, Direction facing) {
		super(world, Actions.BUILD_BRIDGE);
		this.x = x;
		this.y = y;
		this.facing = facing;
	}

	public BuildBridgeAction(World world, CompoundTag nbt) {
		this(world, nbt.getInt("x"), nbt.getInt("y"), Direction.values()[nbt.getInt("facing")]);
	}

	@Override
	public void toNbt(CompoundTag nbt) {
		super.toNbt(nbt);
		nbt.putInt("x", x);
		nbt.putInt("y", y);
		nbt.putInt("facing", facing.ordinal());
	}

	@Override
	protected boolean verify(Symbol symbol) {
		bridge = BridgePlacer.create(world, symbol, x, y, facing);
		return bridge != null && bridge.isFullyValid();
	}

	@Override
	protected void redo(Symbol symbol) {
		parts.clear();

		for (TilePos pos : bridge.getTiles()) {
			parts.add((BridgeStructure) world.addEntity(Entities.BRIDGE, pos.x, pos.y));
		}

		bridge.configure(parts);
		world.getCountry(symbol).addMaterials(-bridge.getCost());
		Sounds.STAMP.play(x, y);
	}

	@Override
	protected void undo(Symbol symbol) {
		for (BridgeStructure part : parts) {
			part.remove();
		}

		world.getCountry(symbol).addMaterials(bridge.getCost());
	}
}
