package net.darktree.game.interactor;

import net.darktree.Main;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.world.Pattern;
import net.darktree.core.world.World;
import net.darktree.core.world.WorldHolder;
import net.darktree.core.world.action.ColonizeAction;
import net.darktree.core.world.action.MoveAction;
import net.darktree.core.world.overlay.PathfinderOverlay;
import net.darktree.core.world.path.Path;
import net.darktree.core.world.path.Pathfinder;
import net.darktree.game.entities.UnitEntity;
import org.lwjgl.glfw.GLFW;

public class MoveInteractor extends Interactor {

	private final Pathfinder pathfinder;
	private final UnitEntity entity;
	private final World world;

	public MoveInteractor(UnitEntity entity, World world) {
		this.entity = entity;
		this.pathfinder = new Pathfinder(world, entity.getX(), entity.getY(), 5, world.getCurrentSymbol(), Pattern.IDENTITY, false);
		this.world = world;

		world.setOverlay(new PathfinderOverlay(pathfinder));
	}

	@Override
	public void draw(VertexBuffer buffer) {
		int x = Main.window.input().getMouseMapX(world.getView());
		int y = Main.window.input().getMouseMapY(world.getView());

		if (world.isPositionValid(x, y) && pathfinder.canReach(x, y)) {
			pathfinder.getPathTo(x, y).draw(WorldHolder.pipeline.buffer);
		}
	}

	@Override
	public void onClick(int button, int action, int mods, int x, int y) {
		if (world.isPositionValid(x, y) && pathfinder.canReach(x, y)) {
			Path path = pathfinder.getPathTo(x, y);
			world.getManager().apply(new MoveAction(entity, path));
			closed = true;
		}
	}

	@Override
	public void onKey(int key, int action, int mods) {
		super.onKey(key, action, mods);

		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_K) {
			world.getManager().apply(new ColonizeAction(entity));
		}
	}

	@Override
	public void close() {
		world.setOverlay(null);
	}

}
