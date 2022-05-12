package net.darktree.game.entities;

import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.util.Type;
import net.darktree.core.world.Pattern;
import net.darktree.core.world.World;
import net.darktree.core.world.entity.Entity;
import net.darktree.core.world.entity.MovingEntity;
import net.darktree.game.country.Symbol;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class UnitEntity extends MovingEntity {

	private Symbol symbol = Symbol.CROSS;
	private boolean colonized = false;

	public UnitEntity(World world, int x, int y, Type<Entity> type) {
		super(world, x, y, type);
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	public Symbol getSymbol() {
		return symbol;
	}

	public void draw(VertexBuffer buffer) {
		super.draw(buffer);
		Renderer.quad(buffer, x, y, 1, 1, symbol.getSprite(), 1, 1, 1, 0);
	}

	public void colonize() {
		if (!colonized && world.getTileState(tx, ty).getOwner() == this.symbol) {
			Pattern.nextColonizationPattern().iterate(world, tx, ty, pos -> {
				world.getTileState(pos.x, pos.y).setOwner(world, pos.x, pos.y, this.symbol);
			});

			colonized = true;
		}
	}

	@Override
	public void revert() {
		if (!colonized) {
			super.revert();
		}
	}

	@Override
	public void toNbt(@NotNull CompoundTag tag) {
		super.toNbt(tag);
		tag.putByte("symbol", (byte) this.symbol.ordinal());
	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {
		super.fromNbt(tag);
		this.symbol = Symbol.values()[tag.getByte("symbol")];
	}

	@Override
	public void onPlayerTurnStart(World world, int x, int y, Symbol symbol) {
		super.onPlayerTurnStart(world, x, y, symbol);
		this.colonized = false;
	}

	@Override
	public boolean canPathfindThrough(Symbol symbol) {
		return this.symbol == symbol;
	}

}
