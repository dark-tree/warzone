package net.darktree.game.buildings;

import net.darktree.core.client.Colors;
import net.darktree.core.client.Sprites;
import net.darktree.core.client.render.color.Color;
import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.util.Type;
import net.darktree.core.world.World;
import net.darktree.core.world.overlay.Overlay;

public class FactoryBuilding extends Building {

	public FactoryBuilding(World world, int x, int y, Type<Building> type) {
		super(world, x, y, type);
	}

	@Override
	public int getCost() {
		return 0;
	}

	@Override
	public void draw(int x, int y, VertexBuffer buffer) {
		Overlay overlay = world.getOverlay();
		Color c = overlay == null ? Colors.OVERLAY_NONE : overlay.getColor(world, x, y, world.getTileState(x, y));

		Renderer.quad(buffer, x, y, 2, 2, Sprites.BUILDING_FACTORY, c.r, c.g, c.b, c.a);
	}
}
