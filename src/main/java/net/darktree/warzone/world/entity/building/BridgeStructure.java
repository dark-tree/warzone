package net.darktree.warzone.world.entity.building;

import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.util.Direction;
import net.darktree.warzone.world.Update;
import net.darktree.warzone.world.Warp;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.action.BridgePlacer;
import net.darktree.warzone.world.entity.Entities;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.tile.TilePos;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BridgeStructure extends FacedStructure implements Warp, MultipartStructure {

	private TilePos a, b;
	private boolean edge;

	public BridgeStructure(WorldSnapshot world, int x, int y) {
		super(world, x, y, Entities.BRIDGE);
	}

	public void configure(TilePos a, TilePos b, boolean edge, Direction facing) {
		this.a = a;
		this.b = b;
		this.edge = edge;
		setFacing(facing);
	}

	public Entity copyFrom(Entity entity) {
		BridgeStructure moving = (BridgeStructure) entity;

		this.a = moving.a;
		this.b = moving.b;
		this.edge = moving.edge;
		return super.copyFrom(entity);
	}

	@Override
	public List<TilePos> getWarpedTiles() {
		return List.of(a, b);
	}

	@Override
	public boolean isWarpDirect() {
		return true;
	}

	@Override
	public boolean canWarpFrom(int x, int y) {
		return a.equals(x, y) || b.equals(x, y);
	}

	@Override
	public void fromNbt(@NotNull CompoundTag nbt) {
		super.fromNbt(nbt);
		this.a = new TilePos(nbt.getInt("ax"), nbt.getInt("ay"));
		this.b = new TilePos(nbt.getInt("bx"), nbt.getInt("by"));
		this.edge = nbt.getBoolean("edge");
	}

	@Override
	public void toNbt(@NotNull CompoundTag nbt) {
		super.toNbt(nbt);
		nbt.putInt("ax", this.a.x);
		nbt.putInt("ay", this.a.y);
		nbt.putInt("bx", this.b.x);
		nbt.putInt("by", this.b.y);
		nbt.putBoolean("edge", this.edge);
	}

	@Override
	public void onAdded() {
		super.onAdded();
		world.pushUpdateBits(Update.CONTROL | Update.OVERLAY);
	}

	@Override
	public void onRemoved() {
		super.onAdded();
		world.pushUpdateBits(Update.CONTROL | Update.OVERLAY);
	}

	@Override
	public boolean isInControl(Symbol symbol) {
		return getOwnerSymbol() == symbol;
	}

	@Override
	public Optional<Country> getOwner() {
		return Optional.ofNullable(world.getCountry(getOwnerSymbol()));
	}

	@Override
	public List<TilePos> getStructureParts() {
		return Objects.requireNonNull(BridgePlacer.create(world, getX(), getY(), facing, true)).getTiles();
	}

	private Symbol getOwnerSymbol() {
		Symbol sa = world.getTileState(this.a).getOwner();
		Symbol sb = world.getTileState(this.b).getOwner();

		boolean ca = world.canControl(a.x, a.y);
		boolean cb = world.canControl(b.x, b.y);

		// both shores are controlled by the same player
		if (ca && cb && (sa == sb)) {
			return sa;
		}

		// one shore (A) is owned and the other (B) is not
		if (ca && sb == Symbol.NONE && sa != Symbol.NONE) {
			return sa;
		}

		// one shore (B) is owned and the other (A) is not
		if (cb && sa == Symbol.NONE && sb != Symbol.NONE) {
			return sb;
		}

		// one shore (A) is controlled and the other (B) is not
		if (ca && !cb) {
			return sa;
		}

		// one shore (B) is controlled and the other (A) is not
		if (cb && !ca) {
			return sb;
		}

		return Symbol.NONE;
	}

}
