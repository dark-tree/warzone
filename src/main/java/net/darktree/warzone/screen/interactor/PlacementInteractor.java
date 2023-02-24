package net.darktree.warzone.screen.interactor;

import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.building.Building;
import net.darktree.warzone.world.tile.TilePos;

public abstract class PlacementInteractor extends Interactor {

	protected final Building.Type type;
	protected final World world;

	protected TilePos pos;
	protected boolean valid;

	protected PlacementInteractor(Building.Type type, World world) {
		this.type = type;
		this.world = world;
		this.pos = new TilePos(0, 0);
	}

	public interface Provider {
		PlacementInteractor create(Building.Type type, World world);
	}

}
