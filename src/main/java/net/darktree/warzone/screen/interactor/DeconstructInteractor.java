package net.darktree.warzone.screen.interactor;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.vertex.Renderer;
import net.darktree.warzone.client.render.vertex.VertexBuffer;
import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.entity.building.Building;

public class DeconstructInteractor extends Interactor {

	private final World world;
	private final Symbol symbol;

	private int x, y;

	public DeconstructInteractor(Symbol symbol, World world) {
		this.world = world;
		this.symbol = symbol;
	}

	@Override
	public void draw(VertexBuffer texture, VertexBuffer color) {
		int x = Main.window.input().getMouseMapX(world.getView());
		int y = Main.window.input().getMouseMapY(world.getView());

		if (world.isPositionValid(x, y) && world.canControl(x, y, this.symbol)) {
			Entity entity = world.getEntity(x, y);

			float wave = (float) (Math.sin(Main.window.profiler.getFrameCount() / 6f) + 1) / 3f;
			Color c = Colors.SPOT_INVALID;

			if (entity != null && entity.isDeconstructable() ) {
				this.x = entity.getX();
				this.y = entity.getY();

				if (entity instanceof Building building) {
					Building.Type type = building.getType();
					Renderer.quad(color, this.x, this.y, type.width, type.height, Sprites.NONE, c.r, c.g, c.b, c.a * wave);
				} else {
					Renderer.quad(color, this.x, this.y, 1, 1, Sprites.NONE, c.r, c.g, c.b, c.a * wave);
				}
			}
		}
	}

	// FIXME: this thing is buggy and half broken
	@Override
	public void onClick(ClickEvent event, int x, int y) {
		if (this.x == x && this.y == y) {
			Entity entity = world.getEntity(this.x, this.y);

			if (entity.isDeconstructable()) {
				entity.deconstruct();
			}
		}
		closed = true;
	}
}
