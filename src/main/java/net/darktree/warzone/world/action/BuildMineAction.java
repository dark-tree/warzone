package net.darktree.warzone.world.action;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.action.manager.Action;
import net.darktree.warzone.world.entity.building.Building;
import net.darktree.warzone.world.entity.building.MineBuilding;
import net.darktree.warzone.world.tile.tiles.Tiles;
import net.querz.nbt.tag.CompoundTag;

public final class BuildMineAction extends Action {

	private final int x, y;

	public BuildMineAction(World world, int x, int y) {
		super(world, Actions.BUILD_MINE);
		this.x = x;
		this.y = y;
	}

	public BuildMineAction(World world, CompoundTag nbt) {
		this(world, nbt.getInt("x"), nbt.getInt("y"));
	}

	@Override
	public void toNbt(CompoundTag nbt) {
		super.toNbt(nbt);
		nbt.putInt("x", x);
		nbt.putInt("y", y);
	}

	@Override
	protected boolean verify(Symbol symbol) {
		return (world.getTileState(x, y).getTile() == Tiles.MATERIAL_ORE) && world.canControl(x, y, symbol);
	}

	@Override
	protected void redo(Symbol symbol) {
		toggle();
	}

	@Override
	protected void undo(Symbol symbol) {
		toggle();
	}

	private void toggle() {
		Building building = world.getEntity(x, y, Building.class);

		if (building == null) {
			world.addEntity(new MineBuilding(world, x, y));
		} else {
			if (building instanceof MineBuilding) {
				building.remove();
			}
		}
	}

}
