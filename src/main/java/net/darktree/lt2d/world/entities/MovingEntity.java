package net.darktree.lt2d.world.entities;

import net.darktree.lt2d.graphics.vertex.VertexBuffer;
import net.darktree.lt2d.world.TilePos;
import net.darktree.lt2d.world.World;
import net.darktree.lt2d.world.path.Path;

public class MovingEntity extends Entity {

	float sx, sy;
	private Path path = null;

	public MovingEntity(World world, int x, int y, Type<?> type) {
		super(world, x, y, type);
	}

	public void move(int x, int y, float speed) {
		this.tx = x;
		this.ty = y;

		sx = (x - this.x) * speed;
		sy = (y - this.y) * speed;
	}

	public void follow(Path path) {
		this.path = path;
		followNext();
	}

	private void followNext() {
		if (path != null) {
			TilePos target = this.path.getNext();

			if (target != null) {
				move(target.x, target.y, 0.05f);
			}else{
				this.path = null;
			}
		}
	}

	@Override
	public void draw(VertexBuffer buffer) {
		super.draw(buffer);

		this.x += sx;
		this.y += sy;

		if (Math.abs(this.x - this.tx) < 0.0001f && Math.abs(this.y - this.ty) < 0.0001f) {
			this.sx = 0;
			this.sy = 0;
			this.x = this.tx;
			this.y = this.ty;
			onTargetReached();
		}
	}

	public void onTargetReached() {
		followNext();
	}

}
