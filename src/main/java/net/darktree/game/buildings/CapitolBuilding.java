package net.darktree.game.buildings;

import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.event.ClickEvent;
import net.darktree.core.util.BuildingType;
import net.darktree.core.world.World;
import net.darktree.game.country.Symbol;
import net.darktree.game.interactor.CityInteractor;
import net.darktree.game.screen.PlayScreen;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class CapitolBuilding extends Building {

	private Symbol symbol = Symbol.CROSS;
	public boolean summoned;

	public CapitolBuilding(World world, int x, int y, BuildingType type) {
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
	public void onPlayerTurnStart(World world, int x, int y, Symbol symbol) {
		if (symbol == this.symbol) summoned = false;
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
		if (!summoned) {
			PlayScreen.setInteractor(new CityInteractor(world.getCurrentSymbol(), world));
		}
	}

	@Override
	public void draw(int x, int y, VertexBuffer buffer) {
		super.draw(x, y, buffer);
		Renderer.quad(buffer, x + 0.5f, y + 0.5f, 1, 1, symbol.getSprite(), 0, 0, 0, 0);
	}

	@Override
	public boolean canAbolish() {
		return false;
	}

}
