package net.darktree.game.interactor;

import net.darktree.Main;
import net.darktree.core.client.Colors;
import net.darktree.core.client.Sprites;
import net.darktree.core.client.render.color.Color;
import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.util.BuildingType;
import net.darktree.core.world.World;
import net.darktree.core.world.entity.Entity;
import net.darktree.core.world.entity.building.Building;
import net.darktree.game.country.Symbol;

public class DeconstructInteractor extends Interactor {

	private final World world;
	private final Symbol symbol;

	private int x, y;

	public DeconstructInteractor(Symbol symbol, World world) {
		this.world = world;
		this.symbol = symbol;
	}

	@Override
	public void draw(VertexBuffer buffer) {
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
					BuildingType type = building.getType();
					Renderer.quad(buffer, this.x, this.y, type.width, type.height, Sprites.NONE, c.r, c.g, c.b, c.a * wave);
				} else {
					Renderer.quad(buffer, this.x, this.y, 1, 1, Sprites.NONE, c.r, c.g, c.b, c.a * wave);
				}
			}
		}
	}

	@Override
	public void onClick(int button, int action, int mods, int x, int y) {
		if (this.x == x && this.y == y) {
			Entity entity = world.getEntity(this.x, this.y);

			if (entity.isDeconstructable()) {
				entity.deconstruct();
			}
		}
		closed = true;
	}
}
