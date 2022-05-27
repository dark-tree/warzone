package net.darktree.game.tiles;

import net.darktree.core.client.window.Window;
import net.darktree.core.event.ClickEvent;
import net.darktree.core.world.World;
import net.darktree.core.world.tile.Tile;
import net.darktree.core.world.tile.TileInstance;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

public class EmptyTile extends Tile {

	@Override
	public void onInteract(World world, int x, int y, ClickEvent event) {
		if (event.isPressed()) {
			world.setTileVariant(x, y, Window.INSTANCE.input().isKeyPressed(GLFW.GLFW_KEY_W) ? Tiles.WATER.getDefaultVariant() : Tiles.MATERIAL.getDefaultVariant());
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

	@Override
	public boolean isReplaceable() {
		return true;
	}
}
