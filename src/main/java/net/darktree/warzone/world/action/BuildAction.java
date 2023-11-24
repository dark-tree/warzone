package net.darktree.warzone.world.action;

import net.darktree.warzone.Registries;
import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.country.Resources;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.util.Direction;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.action.ledger.Action;
import net.darktree.warzone.world.entity.building.Building;
import net.darktree.warzone.world.tile.Surface;
import net.darktree.warzone.world.tile.Tile;
import net.darktree.warzone.world.tile.TilePos;
import net.darktree.warzone.world.tile.TileState;
import net.querz.nbt.tag.CompoundTag;

public final class BuildAction extends Action {

	private final Building.Type type;
	private final int x, y;
	private final Direction facing;

	public BuildAction(Building.Type type, int x, int y, Direction facing) {
		super(Actions.BUILD);
		this.type = type;
		this.x = x;
		this.y = y;
		this.facing = facing;
	}

	public BuildAction(CompoundTag nbt) {
		this((Building.Type) Registries.ENTITIES.byId(nbt.getInt("type")).value(), nbt.getInt("x"), nbt.getInt("y"), Direction.values()[nbt.getInt("facing")]);
	}

	@Override
	public void toNbt(CompoundTag nbt) {
		super.toNbt(nbt);
		nbt.putInt("type", type.id());
		nbt.putInt("x", x);
		nbt.putInt("y", y);
		nbt.putInt("facing", facing.ordinal());
	}

	@Override
	public boolean redo(WorldSnapshot world, boolean animate) {
		Symbol symbol = world.getCurrentSymbol();

		if (!world.getCountry(symbol).getResource(Resources.MATERIALS).has(type.value)) {
			return false;
		}

		for (TilePos pos : type.pattern.list(world, x, y, true)) {
			TileState state = world.getTileState(pos);

			Tile tile = state.getTile();
			if (state.getEntity() != null || state.getOwner() != symbol || tile.getSurface() != Surface.LAND || !tile.canStayOn()) {
				return false;
			}
		}

		((Building) world.addEntity(type, x, y)).setFacing(facing);
		world.getCountry(symbol).addMaterials(-type.value);
		Sounds.STAMP.play(x, y);
		return true;
	}

}
