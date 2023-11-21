package net.darktree.warzone.world.entity.building;

import net.darktree.warzone.client.render.vertex.Renderer;
import net.darktree.warzone.client.render.vertex.VertexBuffer;
import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.screen.PlayScreen;
import net.darktree.warzone.screen.interactor.CityInteractor;
import net.darktree.warzone.world.WorldAccess;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.entity.Entities;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.path.PathFinder;
import net.darktree.warzone.world.path.PathFinderConfig;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class CapitolBuilding extends Building {

	private Symbol symbol;
	public boolean summoned;

	public CapitolBuilding(WorldSnapshot world, int x, int y) {
		super(world, x, y, Entities.CAPITOL);
		this.symbol = world.getTileState(x, y).getOwner();
	}

	public Entity copyFrom(Entity entity) {
		CapitolBuilding moving = (CapitolBuilding) entity;

		this.symbol = moving.symbol;
		this.summoned = moving.summoned;
		return super.copyFrom(entity);
	}

	@Override
	public void toNbt(@NotNull CompoundTag nbt) {
		super.toNbt(nbt);
		nbt.putByte("symbol", (byte) symbol.ordinal());
	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {
		super.fromNbt(tag);
		this.symbol = Symbol.fromNbt("symbol", tag);
	}

	@Override
	public void onPlayerTurnStart(Symbol symbol) {
		if (symbol == this.symbol) summoned = false;
	}

	@Override
	public boolean canPathfindThrough(Symbol symbol) {
		return symbol == this.symbol;
	}

	@Override
	public void onInteract(WorldAccess world, int x, int y, ClickEvent event) {
		if (!summoned && world.getActiveSymbol() == this.symbol) {
			PlayScreen.setInteractor(new CityInteractor(world.getCurrentSymbol(), world));
		}
	}

	@Override
	protected void draw(VertexBuffer buffer) {
		super.draw(buffer);
		Renderer.quad(buffer, tx + 0.5f, ty + 0.5f, 1, 1, symbol.getSprite(), 0, 0, 0, 0);
	}

	@Override
	public boolean isDeconstructable() {
		return false;
	}

	public PathFinder getPathFinder() {
		return new PathFinder(world, world.getCurrentSymbol(), this::forEachTile, PathFinderConfig.UNIT_SUMMON, false);
	}

	public Symbol getSymbol() {
		return symbol;
	}

}
