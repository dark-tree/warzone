package net.darktree.game.tiles;

import net.darktree.game.Tile;
import net.darktree.game.World;
import net.querz.nbt.tag.CompoundTag;

public class EmptyTile extends Tile {

	public EmptyTile(World world) {
		super(world);
	}

	public EmptyTile(World world, CompoundTag tag) {
		super(world);
	}

}
