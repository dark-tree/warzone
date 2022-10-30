package net.darktree.game.interactor;

import net.darktree.Main;
import net.darktree.core.client.Colors;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.util.math.MathHelper;
import net.darktree.core.world.Surface;
import net.darktree.core.world.World;
import net.darktree.core.world.action.ColonizeAction;
import net.darktree.core.world.action.MoveAction;
import net.darktree.core.world.action.ToggleArmorAction;
import net.darktree.core.world.entity.UnitEntity;
import net.darktree.core.world.overlay.PathfinderOverlay;
import net.darktree.core.world.path.Path;
import net.darktree.core.world.path.Pathfinder;
import net.darktree.core.world.pattern.Patterns;
import net.darktree.game.screen.PlayScreen;
import org.lwjgl.glfw.GLFW;

public class UnitInteractor extends Interactor {

	private final Pathfinder pathfinder;
	private final UnitEntity entity;
	private final World world;

	public UnitInteractor(UnitEntity entity, World world) {
		this.entity = entity;
		this.world = world;

		if (!entity.hasMoved()) {
			this.pathfinder = new Pathfinder(world, 5, world.getCurrentSymbol(), Surface.LAND,  Patterns.IDENTITY.place(world, entity.getX(), entity.getY()), false);
			world.setOverlay(new PathfinderOverlay(pathfinder));
		} else {
			this.pathfinder = null;
		}
	}

	@Override
	public void draw(VertexBuffer texture, VertexBuffer color) {
		entity.drawSelection(color, Colors.ENTITY_SELECTION);

		int x = Main.window.input().getMouseMapX(world.getView());
		int y = Main.window.input().getMouseMapY(world.getView());

		if (pathfinder != null && world.isPositionValid(x, y) && pathfinder.canReach(x, y)) {
			pathfinder.getPathTo(x, y).draw(color);
		}
	}

	@Override
	public void onClick(int button, int action, int mods, int x, int y) {
		if (action == GLFW.GLFW_PRESS) {
			if (pathfinder != null && world.isPositionValid(x, y) && pathfinder.canReach(x, y)) {
				Path path = pathfinder.getPathTo(x, y);
				world.getManager().apply(new MoveAction(entity, path));
			}

			closed = true;
		}
	}

	@Override
	public void onKey(int key, int action, int mods) {
		super.onKey(key, action, mods);

		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_K) {
			if (world.getManager().apply(new ColonizeAction(entity, MathHelper.nextRandomDice(), false))) {
				if (entity.isRemoved()) closed = true;
			}
		}

		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_W) {
			if (world.getManager().apply(new ColonizeAction(entity, MathHelper.nextRandomDice(), true))) {
				if (entity.isRemoved()) closed = true;
			}
		}

		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_Z) {
			world.getManager().apply(new ToggleArmorAction(entity));
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
