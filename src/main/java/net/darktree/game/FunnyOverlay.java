package net.darktree.game;

import net.darktree.lt2d.world.World;
import net.darktree.lt2d.world.overlay.Color;
import net.darktree.lt2d.world.overlay.Overlay;

public class FunnyOverlay implements Overlay {

	@Override
	public void getColor(int x, int y, World world, Color color) {
		if (y % 2 == 0) {
			color.set(0.8f, 0.2f, 0.2f, 0.2f);
		}else{
			color.clear();
		}
	}
}
