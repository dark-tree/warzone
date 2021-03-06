package net.darktree.game.interactor;

import net.darktree.Main;
import net.darktree.core.client.Colors;
import net.darktree.core.client.Sprites;
import net.darktree.core.client.render.color.Color;
import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.util.Direction;
import net.darktree.core.util.math.MathHelper;
import net.darktree.core.world.World;
import net.darktree.core.world.action.EntityShotAction;
import net.darktree.core.world.entity.Entity;
import net.darktree.core.world.tile.TilePos;
import net.darktree.game.entities.UnitEntity;

public class UnitAttackInteractor extends Interactor {

	private final UnitEntity entity;
	private final World world;

	public UnitAttackInteractor(UnitEntity entity, World world) {
		this.entity = entity;
		this.world = world;

		world.setOverlay(null);
	}

	@Override
	public void draw(VertexBuffer buffer) {
		entity.drawSelection(buffer, Colors.ENTITY_SELECTION);

		int x = Main.window.input().getMouseMapX(world.getView());
		int y = Main.window.input().getMouseMapY(world.getView());

		if (isValid(world, x, y)) {
			drawAttackVector(buffer, entity.getX(), entity.getY(), x, y, Colors.SPOT_INVALID);
		}
	}

	private boolean isValid(World world, int tx, int ty) {
		int fx = entity.getX();
		int fy = entity.getY();

		if (!world.isPositionValid(tx, ty) || world.getCountry(entity.getSymbol()).ammo == 0) {
			return false;
		}

		int md = MathHelper.getManhattanDistance(fx, fy, tx, ty);
		int cd = MathHelper.getChebyshevDistance(fx, fy, tx, ty);

		if (cd == 0) return false; // the points are equal
		if (md >= 3) return false; // outside the maximum range
		if (cd == 1) return isTargetValid(world, tx, ty); // inside inner circle

		// at this point there are only 4 possible positions
		// each on an axis-aligned line starting from the (fx, fy) point and at a distance of 2 from it,
		// validity of the selection depends then on the tile in the middle between (fx, fy) and (tx, ty)

		TilePos middle = MathHelper.getMiddlePoint(fx, fy, tx, ty);
		Direction vector = MathHelper.getDirection(fx, fy, tx, ty);
		boolean tileCheck = world.getTileState(middle).getTile().canPenetrate(world, middle.x, middle.y, vector);

		return tileCheck && world.getEntity(middle) == null && isTargetValid(world, tx, ty);
	}

	private boolean isTargetValid(World world, int tx, int ty) {
		boolean tileCheck = world.getTileState(tx, ty).getTile().isDestructible(world, tx, ty);
		return tileCheck || world.getEntity(tx, ty) instanceof UnitEntity unit && unit.getSymbol() != entity.getSymbol();
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
				world.getManager().apply(new EntityShotAction(entity, unit));
				closed = true;
			}

		}
	}
}
