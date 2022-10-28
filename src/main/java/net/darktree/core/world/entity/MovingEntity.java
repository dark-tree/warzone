package net.darktree.core.world.entity;

import net.darktree.Main;
import net.darktree.core.client.render.color.Color;
import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.world.World;
import net.darktree.core.world.path.Path;
import net.darktree.core.world.tile.TilePos;
import net.darktree.game.country.Symbol;

public abstract class MovingEntity extends Entity {

	protected float x, y;
	private float sx, sy;
	private Path path = null;
	private int px, py;
	protected boolean moved;

	public MovingEntity(World world, int x, int y, Type type) {
		super(world, x, y, type);

		this.px = x;
		this.py = y;
	}

	public void move(int x, int y, float speed) {
		this.tx = x;
		this.ty = y;

		sx = (x - this.x) * speed;
		sy = (y - this.y) * speed;
	}

	public void follow(Path path) {
		this.path = path;
		this.moved = true;
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
	public void setPos(int x, int y) {
		super.setPos(x, y);

		this.x = x;
		this.y = y;
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

	@Override
	public void onPlayerTurnStart(World world, int x, int y, Symbol symbol) {
		this.px = this.tx;
		this.py = this.ty;
		this.moved = false;
	}

	public void onTargetReached() {
		followNext();
	}

	public void revert() {
		move(px, py, 0.05f);
		moved = false;
		path = null;
	}

	public boolean hasMoved() {
		return moved;
	}

	public void setAttacked(boolean attacked) {
		moved = attacked;
	}

	@Override
	public int getX() {
		return path != null ? path.getEnd().x : tx;
	}

	@Override
	public int getY() {
		return path != null ? path.getEnd().y : ty;
	}

	public void drawSelection(VertexBuffer buffer, Color c) {
		final int x = getX();
		final int y = getY();
		float f = (float) (Math.sin(Main.window.profiler.getFrameCount() / 20f) + 1) / 60 + 0.04f;

		// TODO: make it better
		Renderer.line(buffer, x + f, y + f, x + f, y + f + 0.25f, 0.03f, c);
		Renderer.line(buffer, x + 1 - f, y + f, x + 1 - f, y + 0.25f + f, 0.03f, c);
		Renderer.line(buffer, x + f, y + 1 - f, x + f, y + 0.75f - f, 0.03f, c);
		Renderer.line(buffer, x + 1 - f, y + 1 - f, x + 1 - f, y + 0.75f - f, 0.03f, c);
		Renderer.line(buffer, x + f, y + f, x + 0.25f + f, y + f, 0.03f, c);
		Renderer.line(buffer, x + f, y + 1 - f, x + 0.25f + f, y + 1 - f, 0.03f, c);
		Renderer.line(buffer, x + 1 - f, y + f, x + 0.75f - f, y + f, 0.03f, c);
		Renderer.line(buffer, x + 1 - f, y + 1 - f, x + 0.75f - f, y + 1 - f, 0.03f, c);
	}

}
