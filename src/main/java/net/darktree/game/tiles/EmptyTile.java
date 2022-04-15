package net.darktree.game.tiles;

import net.darktree.lt2d.world.Tile;
import net.darktree.lt2d.world.TileInstance;
import net.darktree.lt2d.world.World;
import net.darktree.lt2d.world.state.property.BooleanProperty;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

public class EmptyTile extends Tile {

	public static BooleanProperty DELETED = new BooleanProperty("deleted");

	public EmptyTile() {

	}

	@Override
	public void onInteract(World world, int x, int y, int mode) {
		Tile tile = world.getTileState(x, y).getTile();

		if (mode == GLFW.GLFW_PRESS && tile == Tiles.EMPTY) {
			world.setTileState(x, y, World.circle ? Tiles.CIRCLE.state : Tiles.CROSS.state);
			World.circle = !World.circle;
		}
	}

	@Override
	public @Nullable TileInstance getInstance(World world, int x, int y) {
		return new EmptyTileInstance(world, x, y, new Random().nextInt());
	}

	public static class EmptyTileInstance extends TileInstance {

		int abc;

		public EmptyTileInstance(World world, int x, int y, int abc) {
			super(world, x, y);
			this.abc = abc;
		}

		@Override
		public void toNbt(@NotNull CompoundTag tag) {
			super.toNbt(tag);
			tag.putInt("abc", abc);
		}

		@Override
		public void fromNbt(@NotNull CompoundTag tag) {
			super.fromNbt(tag);
			this.abc = tag.getInt("abc");
		}

	}

}
