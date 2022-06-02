package net.darktree.game.interactor;

import net.darktree.Main;
import net.darktree.core.client.Sounds;
import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.util.MathHelper;
import net.darktree.core.world.Pattern;
import net.darktree.core.world.World;
import net.darktree.core.world.WorldHolder;
import net.darktree.core.world.action.ArmorAction;
import net.darktree.core.world.action.ColonizeAction;
import net.darktree.core.world.action.MoveAction;
import net.darktree.core.world.overlay.PathfinderOverlay;
import net.darktree.core.world.path.Path;
import net.darktree.core.world.path.Pathfinder;
import net.darktree.game.entities.UnitEntity;
import org.lwjgl.glfw.GLFW;

public class UnitInteractor extends Interactor {

	private final Pathfinder pathfinder;
	private final UnitEntity entity;
	private final World world;

	public UnitInteractor(UnitEntity entity, World world) {
		this.entity = entity;
		this.world = world;

		if (!entity.hasMoved()) {
			this.pathfinder = new Pathfinder(world, entity.getX(), entity.getY(), 5, world.getCurrentSymbol(), Pattern.IDENTITY, false);
			world.setOverlay(new PathfinderOverlay(pathfinder));
		} else {
			this.pathfinder = null;
		}
	}

	@Override
	public void draw(VertexBuffer buffer) {
		int x = entity.getX();
		int y = entity.getY();
		float f = (float) (Math.sin(Main.window.profiler.getFrameCount() / 20f) + 1) / 60 + 0.04f;

		// TODO: make it better
		Renderer.line(buffer, x + f, y + f, x + f, y + f + 0.25f, 0.03f, 0.4f, 0.6f, 1.0f, 1.0f);
		Renderer.line(buffer, x + 1 - f, y + f, x + 1 - f, y + 0.25f + f, 0.03f, 0.4f, 0.6f, 1.0f, 1.0f);
		Renderer.line(buffer, x + f, y + 1 - f, x + f, y + 0.75f - f, 0.03f, 0.4f, 0.6f, 1.0f, 1.0f);
		Renderer.line(buffer, x + 1 - f, y + 1 - f, x + 1 - f, y + 0.75f - f, 0.03f, 0.4f, 0.6f, 1.0f, 1.0f);
		Renderer.line(buffer, x + f, y + f, x + 0.25f + f, y + f, 0.03f, 0.4f, 0.6f, 1.0f, 1.0f);
		Renderer.line(buffer, x + f, y + 1 - f, x + 0.25f + f, y + 1 - f, 0.03f, 0.4f, 0.6f, 1.0f, 1.0f);
		Renderer.line(buffer, x + 1 - f, y + f, x + 0.75f - f, y + f, 0.03f, 0.4f, 0.6f, 1.0f, 1.0f);
		Renderer.line(buffer, x + 1 - f, y + 1 - f, x + 0.75f - f, y + 1 - f, 0.03f, 0.4f, 0.6f, 1.0f, 1.0f);

		x = Main.window.input().getMouseMapX(world.getView());
		y = Main.window.input().getMouseMapY(world.getView());

		if (pathfinder != null && world.isPositionValid(x, y) && pathfinder.canReach(x, y)) {
			pathfinder.getPathTo(x, y).draw(WorldHolder.pipeline.buffer);
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
			if (world.getManager().apply(new ColonizeAction(entity, MathHelper.nextRandomDice()))) {
				Sounds.DICE_ROLL.play(entity).setVolume(2);

				if (entity.removed) {
					closed = true;
				}
			}
		}

		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_Z) {
			world.getManager().apply(new ArmorAction(entity));
		}


//		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_I) {
//			world.setTileOwner(entity.getX(), entity.getY(), Symbol.NONE);
//		}
	}

	@Override
	public void close() {
		world.setOverlay(null);
	}

}
