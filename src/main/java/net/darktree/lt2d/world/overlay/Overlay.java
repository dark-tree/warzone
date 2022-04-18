package net.darktree.lt2d.world.overlay;

import net.darktree.lt2d.world.World;

public interface Overlay {
	void getColor(int x, int y, World world, Color color);
}
