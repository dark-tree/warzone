package net.darktree.warzone.world.action;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.action.ledger.Action;
import net.darktree.warzone.world.entity.building.MineBuilding;
import net.darktree.warzone.world.tile.tiles.Tiles;
import net.querz.nbt.tag.CompoundTag;

public final class BuildMineAction extends Action {

	private final int x, y;

	public BuildMineAction(int x, int y) {
		super(Actions.BUILD_MINE);
		this.x = x;
		this.y = y;
	}

	public BuildMineAction(CompoundTag nbt) {
		this(nbt.getInt("x"), nbt.getInt("y"));
	}

	@Override
	public void toNbt(CompoundTag nbt) {
		super.toNbt(nbt);
		nbt.putInt("x", x);
		nbt.putInt("y", y);
	}

	@Override
	public boolean redo(WorldSnapshot world, boolean animate) {
		Symbol symbol = world.getCurrentSymbol();

		if (world.getTileState(x, y).getTile() != Tiles.MATERIAL_ORE || !world.canControl(x, y, symbol)) {
			return false;
		}

		MineBuilding mine = world.getEntity(x, y, MineBuilding.class);

		if (mine == null) {
			world.addEntity(new MineBuilding(world, x, y));
		} else {
			mine.remove();
		}

		return true;
	}

}
