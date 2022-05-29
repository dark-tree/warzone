package net.darktree.core.world.entity;

import net.darktree.core.Registries;
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
		return this.tx == x && this.ty == y;
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
		tag.putInt("x", tx);
		tag.putInt("y", ty);
	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {

	}

	public static Entity load(World world, @NotNull CompoundTag tag) {
		Entity entity = Registries.ENTITIES.getElement(tag.getString("id")).construct(world, tag.getInt("x"), tag.getInt("y"));
		entity.fromNbt(tag);
		return entity;
	}

}
