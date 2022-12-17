package net.darktree.warzone.world.entity;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.render.WorldBuffers;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.vertex.Renderer;
import net.darktree.warzone.client.render.vertex.VertexBuffer;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.path.Path;
import net.darktree.warzone.world.tile.TilePos;

public abstract class MovingEntity extends Entity {

	protected static final float SPEED = 0.05f;
	protected static final float EPSILON = 0.0001f;

	protected float x, y;
	private float sx, sy;
	private Path path = null;
	private int px, py;
	protected boolean moved, attacked;

	public MovingEntity(World world, int x, int y, Type type) {
		super(world, x, y, type);

		this.px = x;
		this.py = y;
	}

	protected void migrate(int x1, int y1, int x2, int y2) {
		world.getTileState(x1, y1).removeEntity(this);
		world.getTileState(x2, y2).setEntity(this);
		world.markOverlayDirty();
	}

	protected void move(int x, int y) {
		this.tx = x;
		this.ty = y;

		sx = (x - this.x) * SPEED;
		sy = (y - this.y) * SPEED;
	}

	public void follow(Path path) {
		TilePos end = path.getEnd();
		migrate(getX(), getY(), end.x, end.y);

		this.path = path;
		this.moved = true;
		followNext();
	}

	private void followNext() {
		if (path != null) {
			TilePos target = this.path.getNext();

			if (target != null) {
				move(target.x, target.y);
			}else{
				this.path = null;
			}
		}
	}

	@Override
	protected void setPos(int x, int y) {
		super.setPos(x, y);

		this.x = x;
		this.y = y;
	}

	@Override
	public void draw(WorldBuffers buffers, boolean updateStaticElements) {
		draw(buffers.getEntity());
	}

	public void draw(VertexBuffer buffer) {
		this.x += sx;
		this.y += sy;

		if (Math.abs(this.x - this.tx) < EPSILON && Math.abs(this.y - this.ty) < EPSILON) {
			this.sx = 0;
			this.sy = 0;
			this.x = this.tx;
			this.y = this.ty;
			onTargetReached();
		}
	}

	@Override
	public void onPlayerTurnStart(Symbol symbol) {
		this.px = this.tx;
		this.py = this.ty;
		this.moved = false;
		this.attacked = false;
	}

	public void onTargetReached() {
		followNext();
	}

	public void revert() {
		migrate(tx, ty, px, py);
		move(px, py);
		moved = false;
		path = null;
	}

	public boolean hasActed() {
		return moved || attacked;
	}

	public boolean hasMoved() {
		return moved;
	}

	public void setAttacked(boolean attacked) {
		this.attacked = attacked;
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
