package net.darktree.game.buildings;

import net.darktree.core.client.Sprites;
import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.event.ClickEvent;
import net.darktree.core.util.Color;
import net.darktree.core.util.Logger;
import net.darktree.core.util.Type;
import net.darktree.core.world.World;
import net.darktree.core.world.overlay.Overlay;
import net.darktree.game.country.Symbol;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class CapitolBuilding extends Building {

	private Symbol symbol = Symbol.CROSS;
	private final Color c = new Color();

	public CapitolBuilding(World world, int x, int y, Type<Building> type) {
		super(world, x, y, type);
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
	public boolean canPathfindOnto(World world, int x, int y) {
		return false;
	}

	@Override
	public boolean canPathfindThrough(World world, int x, int y) {
		return true;
	}

	@Override
	public void onInteract(World world, int x, int y, ClickEvent event) {
		Logger.info("You clicked me!");
	}

	@Override
	public void draw(int x, int y, VertexBuffer buffer) {
		Overlay overlay = world.getOverlay();

		if (overlay != null) {
			world.getOverlay().getColor(world, x, y, world.getTileState(x, y), c);
		} else c.a = 0;

		Renderer.quad(buffer, x, y, 2, 2, Sprites.BUILDING_CAPITOL, c.r, c.g, c.b, c.a);
		Renderer.quad(buffer, x + 0.5f, y + 0.5f, 1, 1, symbol.getSprite(), c.r, c.g, c.b, c.a);
	}

	@Override
	public int getCost() {
		return 0;
	}

	@Override
	public boolean canAbolish() {
		return false;
	}

}
