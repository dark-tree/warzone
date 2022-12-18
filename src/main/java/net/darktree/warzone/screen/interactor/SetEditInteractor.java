package net.darktree.warzone.screen.interactor;

import net.darktree.warzone.world.World;
import net.darktree.warzone.world.tile.variant.TileVariant;

public class SetEditInteractor extends Interactor {

	private final TileVariant target;
	private final World world;

	public SetEditInteractor(TileVariant target, World world) {
		this.target = target;
		this.world = world;
	}

	@Override
	public void onClick(int button, int action, int mods, int x, int y) {
		if (world.isPositionValid(x, y)) {
			world.setTileVariant(x, y, target);
		}
	}
}
