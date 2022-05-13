package net.darktree.core.world.tile;

import net.darktree.core.Registries;
import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.event.ClickEvent;
import net.darktree.core.world.World;
import net.darktree.core.world.WorldComponent;
import net.darktree.core.world.tile.variant.TileVariant;
import org.jetbrains.annotations.Nullable;

public abstract class Tile implements WorldComponent {
	public final TileVariant variant;

	public Tile() {
		this.variant = createDefaultVariant();
	}

	protected TileVariant createDefaultVariant() {
		return TileVariant.createOf(this);
	}

	public TileVariant getDefaultVariant() {
		return this.variant;
	}

	public void draw(int x, int y, TileState state, VertexBuffer buffer) {
		Renderer.tile(buffer, x, y, state, Registries.ATLAS.getSprite(this));
	}

	public void onInteract(World world, int x, int y, ClickEvent event) {

	}

	public void onRemoved(World world, int x, int y, TileVariant state) {

	}

	public boolean canPathfindThrough(World world, int x, int y) {
		return true;
	}

	/**
	 * Return the Tile Instance for this tile, or null if there should be no tile instance
	 */
	public @Nullable TileInstance getInstance(World world, int x, int y) {
		return null;
	}
}
