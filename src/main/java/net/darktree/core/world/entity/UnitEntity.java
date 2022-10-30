package net.darktree.core.world.entity;

import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.event.ClickEvent;
import net.darktree.core.world.World;
import net.darktree.core.world.pattern.Patterns;
import net.darktree.core.world.tile.TilePos;
import net.darktree.core.world.tiles.Tiles;
import net.darktree.game.country.Symbol;
import net.darktree.game.interactor.UnitInteractor;
import net.darktree.game.screen.PlayScreen;
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
		if (symbol == world.getCurrentSymbol()) {
			PlayScreen.setInteractor(new UnitInteractor(this, world));
		}
	}

	public void colonize(int dice, boolean war) {
		int x = getX();
		int y = getY();

		if (war) {
			Patterns.SMALL_CROSS.iterate(world, x, y, this::warColonizeTile);

			if (dice == 2) {
				Patterns.STAR_SMALL.iterate(world, x, y, this::warColonizeTile);

				Patterns.STAR_LARGE.iterate(world, x, y, pos -> {
					TilePos middle = pos.getMiddlePointFrom(x, y);

					if (warColonizeTile(middle)) {
						warColonizeTile(pos);
					}
				});
			}
		} else {
			Patterns.nextColonizationPattern(dice).iterate(world, x, y, pos -> {
				if (world.getTileState(pos).getOwner() == Symbol.NONE) {
					world.setTileOwner(pos.x, pos.y, this.symbol);
				}
			});
		}

		if (dice % 2 != 0) {
			if (armored) {
				armored = false;
			} else {
				remove();
			}
		}
	}

	private boolean warColonizeTile(TilePos pos) {
		Symbol symbol = world.getTileState(pos.x, pos.y).getOwner();

		if (symbol != Symbol.NONE) {
			Entity entity = world.getEntity(pos);

			if (entity == null || entity instanceof UnitEntity unit && unit.getSymbol() != getSymbol()) {
				if (symbol != getSymbol()) {
					world.setTileOwner(pos.x, pos.y, getSymbol());
				}
				return true;
			}
		}

		return false;
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
