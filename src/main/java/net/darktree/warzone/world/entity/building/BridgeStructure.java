package net.darktree.warzone.world.entity.building;

import net.darktree.warzone.util.Direction;
import net.darktree.warzone.world.Warp;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.Entities;
import net.darktree.warzone.world.tile.TilePos;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BridgeStructure extends FacedStructure implements Warp {

	private TilePos a, b;
	private boolean edge;

	public BridgeStructure(World world, int x, int y) {
		super(world, x, y, Entities.BRIDGE);
	}

	public void configure(TilePos a, TilePos b, boolean edge, Direction facing) {
		this.a = a;
		this.b = b;
		this.edge = edge;
		setFacing(facing);
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

}
