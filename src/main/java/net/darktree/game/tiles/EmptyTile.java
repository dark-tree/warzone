package net.darktree.game.tiles;

import net.darktree.game.Tile;
import net.darktree.game.World;
import net.darktree.game.state.TileState;
import net.darktree.game.state.BooleanProperty;
import net.darktree.game.state.EnumProperty;
import net.darktree.opengl.vertex.Renderer;
import net.darktree.opengl.vertex.VertexBuffer;
import org.lwjgl.glfw.GLFW;

public class EmptyTile extends Tile {

	public EmptyTile() {

	}

	enum State {
		CIRCLE,
		CROSS,
		EMPTY
	}

	public static EnumProperty<State> STATE = new EnumProperty<>(State.class, "state", State.EMPTY);
	public static BooleanProperty DELETED = new BooleanProperty("deleted", false);

	@Override
	protected TileState createDefaultState() {
		return TileState.createOf(this, STATE, DELETED).with(STATE, State.EMPTY);
	}

	@Override
	public void draw(World world, int x, int y, VertexBuffer buffer) {
		super.draw(world, x, y, buffer);

		TileState ts = world.getTileState(x, y);

		State state = ts.get(STATE);

		if(state == State.CIRCLE) Renderer.quad(buffer, x, y, 1, 1, World.CIRCLE);
		if(state == State.CROSS) Renderer.quad(buffer, x, y, 1, 1, World.CROSS);
		if(ts.get(DELETED)) Renderer.quad(buffer, x, y, 1, 1, World.DELETED);
	}

	@Override
	public void onInteract(World world, int x, int y, int mode) {
		State state = world.getTileState(x, y).get(STATE);

		if (mode == GLFW.GLFW_PRESS && state == State.EMPTY) {
			world.setTileState(x, y, world.getTileState(x, y).with(STATE, World.circle ? State.CIRCLE : State.CROSS));
			test(world, x, y);
			World.circle = !World.circle;
		}
	}

	private void test(World world, int x, int y) {
		boolean a = testLine(world, x, y, 1, 0) + testLine(world, x, y, -1, 0) >= 4;
		boolean b = testLine(world, x, y, 0, 1) + testLine(world, x, y, 0, -1) >= 4;
		boolean c = testLine(world, x, y, 1, 1) + testLine(world, x, y, -1, -1) >= 4;
		boolean d = testLine(world, x, y, 1, -1) + testLine(world, x, y, -1, 1) >= 4;

		if(a || b || c || d) {
			world.setTileState(x, y, world.getTileState(x, y).with(DELETED, true));

			if(a) {markLine(world, x, y, 1, 0); markLine(world, x, y, -1, 0);}
			if(b) {markLine(world, x, y, 0, 1); markLine(world, x, y, 0, -1);}
			if(c) {markLine(world, x, y, 1, 1); markLine(world, x, y, -1, -1);}
			if(d) {markLine(world, x, y, 1, -1); markLine(world, x, y, -1, 1);}
		}
	}

	private int testLine(World world, int tx, int ty, int x, int y) {
		int c = 0;

		try {
			for (int i = 1; i < 5; i ++) {
				TileState state = world.getTileState(tx + x * i, ty + y * i);

				if (state.getTile() == Tiles.EMPTY) {
					State stateThis = world.getTileState(tx, ty).get(STATE);
					State stateThat = state.get(STATE);

					if (stateThis == stateThat && !state.get(DELETED)) {
						c++;
					}else{
						break;
					}
				}
			}
		}catch (Exception ignore) {

		}

		return c;
	}

	private void markLine(World world, int tx, int ty, int x, int y) {
		try {
			for (int i = 1; i < 5; i ++) {
				TileState state = world.getTileState(tx + x * i, ty + y * i);

				if (state.getTile() == Tiles.EMPTY) {
					State stateThis = world.getTileState(tx, ty).get(STATE);
					State stateThat = state.get(STATE);

					if (stateThis == stateThat && !(boolean) state.get(DELETED)) {
						world.setTileState(tx + x * i, ty + y * i, state.with(DELETED, true));
					}else{
						break;
					}
				}
			}
		}catch (Exception ignore) {

		}

	}

}
