package net.darktree.warzone.world.entity;

import net.darktree.warzone.client.render.vertex.Renderer;
import net.darktree.warzone.client.render.vertex.VertexBuffer;
import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.country.upgrade.Upgrades;
import net.darktree.warzone.screen.PlayScreen;
import net.darktree.warzone.screen.interactor.UnitInteractor;
import net.darktree.warzone.world.WorldAccess;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.path.PathFinder;
import net.darktree.warzone.world.path.PathFinderConfig;
import net.darktree.warzone.world.pattern.Patterns;
import net.darktree.warzone.world.pattern.ShapeHelper;
import net.darktree.warzone.world.terrain.BonusFinder;
import net.darktree.warzone.world.tile.TileState;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class UnitEntity extends MovingEntity {

	private Symbol symbol = Symbol.CROSS;
	public boolean armored = false;

	public UnitEntity(WorldSnapshot world, int x, int y) {
		super(world, x, y, Entities.UNIT);
	}

	@Override
	public Entity copyFrom(Entity entity) {
		UnitEntity moving = (UnitEntity) entity;

		this.symbol = moving.symbol;
		this.armored = moving.armored;
		return super.copyFrom(entity);
	}

	@Override
	public boolean isOf(Symbol symbol) {
		return this.symbol == symbol;
	}

	/**
	 * Set the symbol of this unit,
	 * this must be called after entity construction, or it will default to CROSS
	 */
	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	/**
	 * Get the symbol of this unit
	 */
	public Symbol getSymbol() {
		return symbol;
	}

	/**
	 * Check if this unit is in its own country
	 */
	public boolean isInHomeland() {
		return world.canControl(getX(), getY(), symbol);
	}

	/**
	 * Colonize at the position of this unit
	 */
	public void colonize(int dice, boolean war) {
		ShapeHelper.TargetPredicate target = (world, x, y) -> colonizationCheck(x, y, war, false);
		ShapeHelper.MidpointPredicate midpoint = (world, direction, tile, pos) -> colonizationCheck(pos.x, pos.y, war, true);

		ShapeHelper.iterateValid(world, target, midpoint, dice == 2, getX(), getY(), pos -> {
			world.getTileState(pos).setOwner(getSymbol(), true);
		});

		// check for and grant any bonus tiles
		BonusFinder finder = new BonusFinder(world, symbol);
		finder.grant();

		if (dice % 2 != 0) {
			if (armored) {
				armored = false;
			} else {
				remove();
			}
		}
	}

	private boolean colonizationOwnerCheck(boolean war, boolean midpoint, Symbol symbol, Symbol self) {
		return midpoint ? (self == symbol) : (war ? symbol != Symbol.NONE : symbol == Symbol.NONE);
	}

	private boolean colonizationCheck(int x, int y, boolean war, boolean midpoint) {
		TileState state = world.getTileState(x, y);
		Entity entity = world.getEntity(x, y);
		boolean allowed = !war || entity == null || entity.canColonize(this.symbol);
		return state.getTile().canColonize(this.symbol) && colonizationOwnerCheck(war, midpoint, state.getOwner(), this.symbol) && allowed;
	}

	@Override
	public int getRange() {
		return world.getCountry(symbol).upgrades.get(Upgrades.MAPS);
	}

	@Override
	protected void draw(VertexBuffer buffer) {
		super.draw(buffer);
		Renderer.quad(buffer, fx, fy, 1, 1, armored ? symbol.getArmoredSprite() : symbol.getSprite(), 1, 1, 1, 0);
	}

	@Override
	public void onInteract(WorldAccess world, int x, int y, ClickEvent event) {
		if (symbol == world.getActiveSymbol()) {
			PlayScreen.setInteractor(new UnitInteractor(this, world));
		}
	}

	@Override
	public boolean canColonize(Symbol enemy) {
		return enemy == this.symbol;
	}

	@Override
	public void toNbt(@NotNull CompoundTag tag) {
		super.toNbt(tag);
		tag.putByte("symbol", (byte) this.symbol.ordinal());
		tag.putBoolean("armored", armored);
	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {
		super.fromNbt(tag);
		this.symbol = Symbol.values()[tag.getByte("symbol")];
		this.armored = tag.getBoolean("armored");
	}

	@Override
	public boolean canPathfindThrough(Symbol symbol) {
		return this.symbol == symbol;
	}

	public PathFinder getPathFinder(boolean extended) {
		return new PathFinder(world, symbol, Patterns.IDENTITY.place(world, getX(), getY()), PathFinderConfig.getForUnitAt(this, world.getTileState(getX(), getY()).getOwner()), extended);
	}

}
