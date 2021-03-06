package net.darktree.core.world.entity;

import net.darktree.Main;
import net.darktree.core.Registries;
import net.darktree.core.client.render.color.Color;
import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.util.NbtSerializable;
import net.darktree.core.util.Type;
import net.darktree.core.world.World;
import net.darktree.core.world.WorldListener;
import net.darktree.game.country.Symbol;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public abstract class Entity implements NbtSerializable, WorldListener {

	protected final World world;
	protected int tx, ty;
	protected float x, y;

	public final Type<Entity> type;
	public boolean removed = false;

	public Entity(World world, int x, int y, Type<Entity> type) {
		this.world = world;
		this.type = type;

		setPos(x, y);
	}

	public boolean isAt(int x, int y) {
		return getX() == x && getY() == y;
	}

	public int getX() {
		return tx;
	}

	public int getY() {
		return ty;
	}

	public void setPos(int x, int y) {
		this.x = this.tx = x;
		this.y = this.ty = y;
	}

	public boolean canPathfindThrough(Symbol symbol) {
		return true;
	}

	public void draw(VertexBuffer buffer) {

	}

	@Override
	public void toNbt(@NotNull CompoundTag tag) {
		tag.putString("id", Registries.ENTITIES.keyOf(this.type));
		tag.putInt("x", getX());
		tag.putInt("y", getY());
	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {

	}

	public static Entity load(World world, @NotNull CompoundTag tag) {
		Entity entity = Registries.ENTITIES.getElement(tag.getString("id")).construct(world, tag.getInt("x"), tag.getInt("y"));
		entity.fromNbt(tag);
		return entity;
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
