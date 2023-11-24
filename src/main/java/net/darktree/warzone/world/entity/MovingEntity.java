package net.darktree.warzone.world.entity;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.render.WorldBuffers;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.vertex.Renderer;
import net.darktree.warzone.client.render.vertex.VertexBuffer;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.Update;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.path.Path;
import net.darktree.warzone.world.tile.TilePos;

public abstract class MovingEntity extends Entity {

	protected static final float SPEED = 0.05f;
	protected static final float EPSILON = 0.001f;

	protected float fx, fy; // current frame position
	private float sx, sy; // movement vector
	private int px, py; // previous tile position
	private Path path = null;
	protected boolean moved, attacked;

	public MovingEntity(WorldSnapshot world, int x, int y, Type type) {
		super(world, x, y, type);

		this.px = x;
		this.py = y;
		this.fx = x;
		this.fy = y;
	}

	public Entity copyFrom(Entity entity) {
		MovingEntity moving = (MovingEntity) entity;

		this.fx = moving.fx;
		this.fy = moving.fy;
		this.sx = moving.sx;
		this.sy = moving.sy;
		this.px = moving.px;
		this.py = moving.py;
		this.moved = moving.moved;
		this.attacked = moving.attacked;
		this.path = null; // TODO is this right?
		return super.copyFrom(entity);
	}

	/**
	 * Make this entity move along the given Path
	 */
	public void follow(Path path) {
		TilePos end = path.getEnd();
		migrate(getX(), getY(), end.x, end.y);
		path.getNext();

		this.path = path;
		this.moved = true;
		followNext();
	}

	/**
	 * Makes this entity teleport to the given target
	 */
	public void setPos(int x, int y) {
		migrate(getX(), getY(), x, y);

		this.px = tx;
		this.py = ty;
		this.tx = x;
		this.ty = y;
		this.fx = x;
		this.fy = y;
		this.sx = 0;
		this.sy = 0;
		this.moved = true;
	}

	/**
	 * Revert the last movement of this entity
	 */
	public void revert() {
		migrate(tx, ty, px, py);
		move(px, py, false);
		moved = false;
		path = null;
	}

	/**
	 * Check if this entity already acted on this turn (attacked or moved)
	 */
	public boolean hasActed() {
		return moved || attacked;
	}

	/**
	 * Check if this entity already moved on this turn
	 */
	public boolean hasMoved() {
		return moved;
	}

	/**
	 * Set the attack flag for this entity
	 */
	public void setAttacked(boolean attacked) {
		this.attacked = attacked;
	}

	/**
	 * Called every time the entity reaches the next point on the given path
	 */
	protected void onTargetReached() {
		followNext();
	}

	/**
	 * Return the pathfinder range for this entity
	 */
	public abstract int getRange();

	protected void migrate(int x1, int y1, int x2, int y2) {
		world.getTileState(x1, y1).removeEntity(this);
		world.getTileState(x2, y2).setEntity(this);
		world.pushUpdateBits(Update.OVERLAY);
	}

	protected void move(int x, int y, boolean match) {
		this.tx = x;
		this.ty = y;

		float a = (x - this.fx), b = (y - this.fy);
		float d = match ? Math.max(1, (float) Math.floor(Math.sqrt(a * a + b * b))) : 1;

		sx = a / d * SPEED;
		sy = b / d * SPEED;
	}

	private void followNext() {
		if (path != null) {
			TilePos target = this.path.getNext();

			if (target != null) {
				move(target.x, target.y, true);
			}else{
				this.path = null;
			}
		}
	}

	@Override
	public void draw(WorldBuffers buffers, boolean updateStaticElements) {
		draw(buffers.getEntity());
	}

	protected void draw(VertexBuffer buffer) {
		this.fx += sx;
		this.fy += sy;

		if (Math.abs(this.fx - this.tx) < EPSILON && Math.abs(this.fy - this.ty) < EPSILON) {
			this.sx = 0;
			this.sy = 0;
			this.fx = this.tx;
			this.fy = this.ty;
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

	@Override
	public int getX() {
		return path != null ? path.getEnd().x : tx;
	}

	@Override
	public int getY() {
		return path != null ? path.getEnd().y : ty;
	}

	/**
	 * Draw a selection around this entity into the given buffer
	 */
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
