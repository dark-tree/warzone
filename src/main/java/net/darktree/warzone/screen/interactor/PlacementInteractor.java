package net.darktree.warzone.screen.interactor;

import net.darktree.warzone.world.WorldAccess;
import net.darktree.warzone.world.entity.building.Building;
import net.darktree.warzone.world.tile.TilePos;

public abstract class PlacementInteractor extends Interactor {

	protected final Building.Type type;
	protected final WorldAccess world;
	protected final boolean play;

	protected TilePos pos;
	protected boolean valid;

	protected PlacementInteractor(Building.Type type, WorldAccess world, boolean play) {
		this.type = type;
		this.world = world;
		this.play = play;
		this.pos = new TilePos(0, 0);
	}

	public interface Provider {
		PlacementInteractor create(Building.Type type, WorldAccess world, boolean play);
	}

}
