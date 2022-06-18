package net.darktree.game.tiles;

import net.darktree.core.event.ClickEvent;
import net.darktree.core.world.World;
import net.darktree.core.world.action.ToggleMineAction;
import net.darktree.core.world.tile.MaterialProvider;
import net.darktree.core.world.tile.Tile;
import net.darktree.core.world.tile.TilePos;
import net.darktree.core.world.tile.variant.TileVariant;
import net.darktree.game.country.Symbol;

public class MaterialMineTile extends Tile implements MaterialProvider {

	public boolean canPathfindThrough(World world, int x, int y) {
		return false;
	}

	@Override
	public void onInteract(World world, int x, int y, ClickEvent event) {
		if (event.isPressed()) {
			world.getManager().apply(new ToggleMineAction(x, y));
		}
	}

	@Override
	public int getIncome() {
		return 1;
	}

	@Override
	public void onAdded(World world, int x, int y, TileVariant state) {
		addMineTo(world, world.getTileState(x, y).getOwner(), x, y);
	}

	@Override
	public void onRemoved(World world, int x, int y, TileVariant state) {
		removeMineFrom(world, world.getTileState(x, y).getOwner(), x, y);
	}

	@Override
	public void onOwnerUpdate(World world, int x, int y, Symbol previous, Symbol current) {
		removeMineFrom(world, previous, x, y);
		addMineTo(world, current, x, y);
	}

	private void removeMineFrom(World world, Symbol symbol, int x, int y) {
		if (symbol != Symbol.NONE) {
			world.getCountry(symbol).removeMine(this, new TilePos(x, y));
		}
	}

	private void addMineTo(World world, Symbol symbol, int x, int y) {
		if (symbol != Symbol.NONE && world.canControl(x, y)) {
			world.getCountry(symbol).addMine(this, new TilePos(x, y));
		}
	}

	@Override
	public boolean isDeconstructable(World world, int x, int y) {
		return true;
	}

	@Override
	public void deconstruct(World world, int x, int y) {
		world.getManager().apply(new ToggleMineAction(x, y));
	}

}
