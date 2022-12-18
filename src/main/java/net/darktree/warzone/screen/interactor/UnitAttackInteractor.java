package net.darktree.warzone.screen.interactor;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.vertex.Renderer;
import net.darktree.warzone.client.render.vertex.VertexBuffer;
import net.darktree.warzone.country.Resources;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.action.EntityShotAction;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.entity.UnitEntity;
import net.darktree.warzone.world.pattern.ShapeHelper;

public class UnitAttackInteractor extends Interactor {

	private final UnitEntity entity;
	private final World world;

	public UnitAttackInteractor(UnitEntity entity, World world) {
		this.entity = entity;
		this.world = world;

		world.getView().hideOverlay();
	}

	@Override
	public void draw(VertexBuffer texture, VertexBuffer color) {
		entity.drawSelection(color, Colors.ENTITY_SELECTION);

		int x = Main.window.input().getMouseMapX(world.getView());
		int y = Main.window.input().getMouseMapY(world.getView());

		if (isValid(world, x, y)) {
			drawAttackVector(color, entity.getX(), entity.getY(), x, y, Colors.SPOT_INVALID);
		}
	}

	private boolean isValid(World world, int tx, int ty) {
		int ammo = world.getCountry(entity.getSymbol()).getResource(Resources.AMMO).value;
		int fx = entity.getX();
		int fy = entity.getY();

		return ammo > 0 && ShapeHelper.isValid(world, this::isTargetValid, (world1, direction, tile, pos) -> {
			return tile == null || tile.canPenetrate(direction);
		}, true, fx, fy, tx, ty);
	}

	private boolean isTargetValid(World world, int tx, int ty) {
		Entity tile = world.getEntity(tx, ty);
		boolean tileCheck = tile != null && tile.isDestructible();
		return tileCheck || tile instanceof UnitEntity unit && unit.getSymbol() != entity.getSymbol();
	}

	private void drawAttackVector(VertexBuffer buffer, int fx, int fy, int tx, int ty, Color color) {
		Renderer.quad(buffer, fx + 0.4f, fy + 0.4f, 0.2f, 0.2f, Sprites.NONE, color.r, color.g, color.b, color.a);
		Renderer.line(buffer, fx + 0.5f, fy + 0.5f, tx + 0.5f, ty + 0.5f, 0.05f, color);
	}

	@Override
	public void onClick(int button, int action, int mods, int x, int y) {
		if (isValid(world, x, y)) {

			Entity target = world.getEntity(x, y);

			if (target instanceof UnitEntity unit) {
				world.getManager().apply(new EntityShotAction(world, entity.getX(), entity.getY(), unit.getX(), unit.getY()));
				closed = true;
			}

		}
	}
}
