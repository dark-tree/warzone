package net.darktree.game.buildings;

import net.darktree.core.client.Sprites;
import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.event.ClickEvent;
import net.darktree.core.util.Logger;
import net.darktree.core.util.Type;
import net.darktree.core.world.World;
import net.darktree.game.country.Symbol;

public class CapitolBuilding extends Building {

	public CapitolBuilding(World world, int x, int y, Type<Building> type) {
		super(world, x, y, type);
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
		Renderer.quad(buffer, x, y, 2, 2, Sprites.BUILDING_CAPITOL, 1, 1, 1, 0);
		Renderer.quad(buffer, x + 0.5f, y + 0.5f, 1, 1, Symbol.TRIANGLE.getSprite(), 1, 1, 1, 0);
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
