package net.darktree.game.tiles;

import net.darktree.game.Tile;
import net.darktree.game.World;
import net.darktree.opengl.vertex.Renderer;
import net.darktree.opengl.vertex.VertexBuffer;
import net.darktree.util.Logger;
import net.querz.nbt.tag.CompoundTag;
import org.lwjgl.glfw.GLFW;

public class EmptyTile extends Tile {

	boolean circle = false;
	boolean cross = false;
	boolean deleted = false;

	public EmptyTile(World world, int x, int y) {
		super(world, x, y);
	}

	public EmptyTile(World world, CompoundTag tag, int x, int y) {
		super(world, x, y);
	}

	@Override
	public void draw(VertexBuffer buffer, float x, float y, float scale) {
		super.draw(buffer, x, y, scale);
		if(circle) Renderer.quad(buffer, x, y, scale, scale, this.world.CIRCLE);
		if(cross) Renderer.quad(buffer, x, y, scale, scale, this.world.CROSS);
		if(deleted) Renderer.quad(buffer, x, y, scale, scale, this.world.DELETED);
	}

	@Override
	public void interact(int mode) {
		if (mode == GLFW.GLFW_PRESS && !circle && !cross) {
			if (this.world.circle) {
				circle = true;
			}else {
				cross = true;
			}

			test();

			this.world.circle = !this.world.circle;
		}
	}

	private void test() {
		boolean a = testLine(1, 0) + testLine(-1, 0) >= 4;
		boolean b = testLine(0, 1) + testLine(0, -1) >= 4;
		boolean c = testLine(1, 1) + testLine(-1, -1) >= 4;
		boolean d = testLine(1, -1) + testLine(-1, 1) >= 4;

		if(a || b || c || d) {
			Logger.info("GAME OVER! ", this.x, " ", this.y);

			this.deleted = true;

			if(a) {markLine(1, 0); markLine(-1, 0);}
			if(b) {markLine(0, 1); markLine(0, -1);}
			if(c) {markLine(1, 1); markLine(-1, -1);}
			if(d) {markLine(1, -1); markLine(-1, 1);}
		}
	}

	private int testLine(int x, int y) {
		int c = 0;

		try {
			for (int i = 1; i < 5; i ++) {
				if (this.world.getTile(this.x + x * i, this.y + y * i) instanceof EmptyTile tile) {
					if (this.circle == tile.circle && this.cross == tile.cross && !tile.deleted) {
						c++;
					}else{
						break;
					}
				}
			}
		}catch (Exception ignore) {

		}

		return c;
	}

	private void markLine(int x, int y) {
		try {
			for (int i = 1; i < 5; i ++) {
				if (this.world.getTile(this.x + x * i, this.y + y * i) instanceof EmptyTile tile) {
					if (this.circle == tile.circle && this.cross == tile.cross && !tile.deleted) {
						tile.deleted = true;
					}else{
						break;
					}
				}
			}
		}catch (Exception ignore) {

		}

	}

}
