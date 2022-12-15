package net.darktree.warzone.world.entity;

import net.darktree.warzone.client.render.vertex.Renderer;
import net.darktree.warzone.client.render.vertex.VertexBuffer;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.event.ClickEvent;
import net.darktree.warzone.screen.PlayScreen;
import net.darktree.warzone.screen.interactor.UnitInteractor;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.pattern.ShapeHelper;
import net.darktree.warzone.world.tile.TileState;
import net.darktree.warzone.world.tile.tiles.Tiles;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class UnitEntity extends MovingEntity {

	private Symbol symbol = Symbol.CROSS;
	public boolean armored = false;

	public UnitEntity(World world, int x, int y) {
		super(world, x, y, Tiles.UNIT);
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	public Symbol getSymbol() {
		return symbol;
	}

	@Override
	public void draw(VertexBuffer buffer) {
		super.draw(buffer);
		Renderer.quad(buffer, x, y, 1, 1, armored ? symbol.getArmoredSprite() : symbol.getSprite(), 1, 1, 1, 0);
	}

	@Override
	public void onInteract(World world, int x, int y, ClickEvent event) {
		if (symbol == world.getActiveSymbol()) {
			PlayScreen.setInteractor(new UnitInteractor(this, world));
		}
	}

	@Override
	public boolean canColonize(Symbol enemy) {
		return enemy == this.symbol;
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

	public void colonize(int dice, boolean war) {
		ShapeHelper.TargetPredicate target = (world, x, y) -> colonizationCheck(x, y, war, false);
		ShapeHelper.MidpointPredicate midpoint = (world, direction, tile, pos) -> colonizationCheck(pos.x, pos.y, war, true);

		ShapeHelper.iterateValid(world, target, midpoint, dice == 2, getX(), getY(), pos -> {
			world.setTileOwner(pos.x, pos.y, getSymbol());
		});

		if (dice % 2 != 0) {
			if (armored) {
				armored = false;
			} else {
				remove();
			}
		}
	}

	public boolean isInHomeland() {
		return world.canControl(getX(), getY(), symbol);
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

}
