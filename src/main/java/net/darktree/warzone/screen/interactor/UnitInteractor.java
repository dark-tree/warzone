package net.darktree.warzone.screen.interactor;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.render.vertex.VertexBuffer;
import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.client.window.input.KeyEvent;
import net.darktree.warzone.network.packet.ColonizePacket;
import net.darktree.warzone.screen.PlayScreen;
import net.darktree.warzone.world.WorldAccess;
import net.darktree.warzone.world.action.MoveUnitAction;
import net.darktree.warzone.world.action.ToggleArmorAction;
import net.darktree.warzone.world.entity.UnitEntity;
import net.darktree.warzone.world.overlay.PathFinderOverlay;
import net.darktree.warzone.world.path.PathFinder;
import org.lwjgl.glfw.GLFW;

public class UnitInteractor extends Interactor {

	private final PathFinder pathfinder;
	private final UnitEntity entity;
	private final WorldAccess world;

	public UnitInteractor(UnitEntity entity, WorldAccess world) {
		this.entity = entity;
		this.pathfinder = entity.getPathFinder(false);
		this.world = world;

		if (!entity.hasActed()) {
			world.getView().setOverlay(new PathFinderOverlay(pathfinder));
		} else {
			closed = true;
		}
	}

	@Override
	public void draw(VertexBuffer texture, VertexBuffer color) {
		if (entity.isRemoved()) closed = true;

		entity.drawSelection(color, Colors.ENTITY_SELECTION);

		int x = Main.window.input().getMouseTileX(world.getView());
		int y = Main.window.input().getMouseTileY(world.getView());

		if (!closed && world.isPositionValid(x, y) && pathfinder.canReach(x, y)) {
			pathfinder.getPathTo(x, y).draw(color);
		}
	}

	@Override
	public void onClick(ClickEvent event, int x, int y) {
		if (event.isPressed()) {
			if (!closed && world.isPositionValid(x, y) && pathfinder.canReach(x, y)) {
				world.getLedger().push(new MoveUnitAction(entity.getX(), entity.getY(), x, y));
			}

			closed = true;
		}
	}

	@Override
	public void onKey(KeyEvent event) {
		super.onKey(event);

		if (event.isPressed(GLFW.GLFW_KEY_K)) {
			new ColonizePacket(entity.getX(), entity.getY(), false).sendToHost();
		}

		if (event.isPressed(GLFW.GLFW_KEY_W)) {
			new ColonizePacket(entity.getX(), entity.getY(), true).sendToHost();
		}

		if (event.isPressed(GLFW.GLFW_KEY_Z)) {
			world.getLedger().push(new ToggleArmorAction(entity.getX(), entity.getY()));
		}

		if (event.isPressed(GLFW.GLFW_KEY_A) && !entity.hasActed()) {
			PlayScreen.setInteractor(new UnitAttackInteractor(entity, world));
		}
	}

	@Override
	public void close() {
		world.getView().hideOverlay();
	}

}
