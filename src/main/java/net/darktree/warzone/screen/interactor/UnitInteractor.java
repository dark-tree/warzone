package net.darktree.warzone.screen.interactor;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.render.vertex.VertexBuffer;
import net.darktree.warzone.screen.PlayScreen;
import net.darktree.warzone.util.math.MathHelper;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.action.ColonizeAction;
import net.darktree.warzone.world.action.MoveUnitAction;
import net.darktree.warzone.world.action.ToggleArmorAction;
import net.darktree.warzone.world.entity.UnitEntity;
import net.darktree.warzone.world.overlay.PathfinderOverlay;
import org.lwjgl.glfw.GLFW;

public class UnitInteractor extends Interactor {

	private final MoveUnitAction action;
	private final UnitEntity entity;
	private final World world;

	public UnitInteractor(UnitEntity entity, World world) {
		this.entity = entity;
		this.world = world;

		if (!entity.hasMoved()) {
			this.action = new MoveUnitAction(world, entity.getX(), entity.getY());
			world.setOverlay(new PathfinderOverlay(action.getPathfinder()));
		} else {
			this.action = null;
		}
	}

	@Override
	public void draw(VertexBuffer texture, VertexBuffer color) {
		entity.drawSelection(color, Colors.ENTITY_SELECTION);

		int x = Main.window.input().getMouseMapX(world.getView());
		int y = Main.window.input().getMouseMapY(world.getView());

		if (action != null && world.isPositionValid(x, y) && action.getPathfinder().canReach(x, y)) {
			action.getPathfinder().getPathTo(x, y).draw(color);
		}
	}

	@Override
	public void onClick(int button, int action, int mods, int x, int y) {
		if (action == GLFW.GLFW_PRESS) {
			if (this.action != null && world.isPositionValid(x, y)) {
				if (this.action.setTarget(x, y)) {
					world.getManager().apply(this.action);
				}
			}

			closed = true;
		}
	}

	@Override
	public void onKey(int key, int action, int mods) {
		super.onKey(key, action, mods);

		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_K) {
			if (world.getManager().apply(new ColonizeAction(world, MathHelper.nextRandomDice(), entity.getX(), entity.getY(), false))) {
				if (entity.isRemoved()) closed = true;
			}
		}

		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_W) {
			if (world.getManager().apply(new ColonizeAction(world, MathHelper.nextRandomDice(), entity.getX(), entity.getY(), true))) {
				if (entity.isRemoved()) closed = true;
			}
		}

		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_Z) {
			world.getManager().apply(new ToggleArmorAction(world, entity.getX(), entity.getY()));
		}

		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_A && !entity.hasMoved()) {
			PlayScreen.setInteractor(new UnitAttackInteractor(entity, world));
		}
	}

	@Override
	public void close() {
		world.setOverlay(null);
	}

}
