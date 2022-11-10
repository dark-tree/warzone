package net.darktree.warzone.world.action;

import net.darktree.warzone.Registries;
import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.action.manager.Action;
import net.darktree.warzone.world.entity.building.Building;
import net.darktree.warzone.world.tile.Surface;
import net.darktree.warzone.world.tile.Tile;
import net.darktree.warzone.world.tile.TilePos;
import net.darktree.warzone.world.tile.TileState;
import net.querz.nbt.tag.CompoundTag;

public final class BuildAction extends Action {

	private final Building.Type type;
	private final int x, y;

	private Building building;

	public BuildAction(World world, Building.Type type, int x, int y) {
		super(world, Actions.BUILD);
		this.type = type;
		this.x = x;
		this.y = y;
	}

	public BuildAction(World world, CompoundTag nbt) {
		this(world, (Building.Type) Registries.ENTITIES.getElement(nbt.getInt("type")), nbt.getInt("x"), nbt.getInt("y"));
	}

	@Override
	public void toNbt(CompoundTag nbt) {
		super.toNbt(nbt);
		nbt.putInt("type", type.id());
		nbt.putInt("x", x);
		nbt.putInt("y", y);
	}

	@Override
	protected boolean verify(Symbol symbol) {
		if (type.value > world.getCountry(symbol).getTotalMaterials()) {
			return false;
		}

		for (TilePos pos : type.pattern.list(world, x, y, true)) {
			TileState state = world.getTileState(pos);

			Tile tile = state.getTile();
			if (state.getEntity() != null || state.getOwner() != symbol || tile.getSurface() != Surface.LAND || !tile.canStayOn()) {
				return false;
			}
		}

		return true;
	}

	@Override
	protected void redo(Symbol symbol) {
		building = (Building) world.addEntity(type, x, y);
		world.getCountry(symbol).addMaterials(-type.value);
		Sounds.STAMP.play(x, y);
	}

	@Override
	protected void undo(Symbol symbol) {
		building.remove();
		world.getCountry(symbol).addMaterials(type.value);
	}

}
