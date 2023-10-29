package net.darktree.warzone.screen.interactor;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.vertex.Renderer;
import net.darktree.warzone.client.render.vertex.VertexBuffer;
import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.entity.building.Building;
import net.darktree.warzone.world.entity.building.MultipartStructure;
import net.darktree.warzone.world.tile.TilePos;

public class DeconstructInteractor extends Interactor {

	private final World world;
	private final Symbol symbol;

	private int x, y;
	private int frame = 0;

	public DeconstructInteractor(Symbol symbol, World world) {
		this.world = world;
		this.symbol = symbol;
	}

	@Override
	public void draw(VertexBuffer texture, VertexBuffer color) {
		int x = Main.window.input().getMouseTileX(world.getView());
		int y = Main.window.input().getMouseTileY(world.getView());

		if (world.isPositionValid(x, y)) {
			Entity entity = world.getEntity(x, y);

			if (entity == null || !entity.isInControl(symbol) || !entity.isDeconstructable()) {
				frame = 0;
				return;
			}

			float wave = (float) (Math.sin(frame / 6f - Math.PI/2) + 1) / 3f;
			Color c = Colors.SPOT_INVALID;

			this.x = x;
			this.y = y;

			if (entity instanceof Building building) {
				if (entity instanceof MultipartStructure multipart) {
					for (TilePos pos : multipart.getStructureParts()) {
						Building part = world.getEntity(pos.x, pos.y, Building.class);

						if (part != null) {
							drawBuildingOverlay(color, part, c, wave);
						}
					}
				} else {
					drawBuildingOverlay(color, building, c, wave);
				}
			} else {
				Renderer.quad(color, entity.getX(), entity.getY(), 1, 1, Sprites.BLANK, c.r(), c.g(), c.b(), c.a() * wave);
			}

			frame ++;
		} else {
			frame = 0;
		}
	}

	private void drawBuildingOverlay(VertexBuffer color, Building building, Color c, float wave) {
		Building.Type type = building.getType();
		Renderer.quad(color, building.getX(), building.getY(), type.width, type.height, Sprites.BLANK, c.r(), c.g(), c.b(), c.a() * wave);
	}

	@Override
	public void onClick(ClickEvent event, int x, int y) {
		if (this.x == x && this.y == y) {
			Entity entity = world.getEntity(this.x, this.y);

			if (entity != null && entity.isDeconstructable()) {
				entity.deconstruct();
				Sounds.DRAW_THING.play(x, y);
			}
		}
		closed = true;
	}
}
