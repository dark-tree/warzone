package net.darktree.warzone.screen.interactor;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.render.vertex.VertexBuffer;
import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.client.window.input.KeyEvent;
import net.darktree.warzone.network.packet.ColonizePacket;
import net.darktree.warzone.screen.PlayScreen;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.action.MoveUnitAction;
import net.darktree.warzone.world.action.ToggleArmorAction;
import net.darktree.warzone.world.entity.UnitEntity;
import net.darktree.warzone.world.overlay.PathFinderOverlay;
import org.lwjgl.glfw.GLFW;

public class UnitInteractor extends Interactor {

	private final MoveUnitAction action;
	private final UnitEntity entity;
	private final World world;

	public UnitInteractor(UnitEntity entity, World world) {
		this.entity = entity;
		this.world = world;

		if (!entity.hasActed()) {
			this.action = new MoveUnitAction(world, entity.getX(), entity.getY());
			world.getView().setOverlay(new PathFinderOverlay(action.getPathfinder()));
		} else {
			this.action = null;
		}
	}

	@Override
	public void draw(VertexBuffer texture, VertexBuffer color) {
		if (entity.isRemoved()) closed = true;

		entity.drawSelection(color, Colors.ENTITY_SELECTION);

		int x = Main.window.input().getMouseMapX(world.getView());
		int y = Main.window.input().getMouseMapY(world.getView());

		if (action != null && world.isPositionValid(x, y) && action.getPathfinder().canReach(x, y)) {
			action.getPathfinder().getPathTo(x, y).draw(color);
		}
	}

	@Override
	public void onClick(ClickEvent event, int x, int y) {
		if (event.isPressed()) {
			if (this.action != null && world.isPositionValid(x, y)) {
				if (this.action.setTarget(x, y)) {
					world.getManager().apply(this.action);
				}
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
			world.getManager().apply(new ToggleArmorAction(world, entity.getX(), entity.getY()));
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
