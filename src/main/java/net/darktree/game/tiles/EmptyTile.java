package net.darktree.game.tiles;

import net.darktree.game.Tile;
import net.darktree.game.World;
import net.darktree.game.state.BooleanProperty;
import net.darktree.game.state.EnumProperty;
import net.darktree.opengl.vertex.Renderer;
import net.darktree.opengl.vertex.VertexBuffer;
import net.darktree.util.Logger;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class EmptyTile extends Tile {

	enum State {
		CIRCLE,
		CROSS,
		EMPTY
	}

	public static EnumProperty<State> STATE = new EnumProperty<>(State.class, "state", State.EMPTY);
	public static BooleanProperty DELETED = new BooleanProperty("deleted", false);

	public EmptyTile(Type type, World world, @Nullable CompoundTag tag, int x, int y) {
		super(type, world, tag, x, y);
	}

	@Override
	public void draw(VertexBuffer buffer, float x, float y) {
		super.draw(buffer, x, y);

		State state = this.state.get(STATE);

		if(state == State.CIRCLE) Renderer.quad(buffer, x, y, 1, 1, World.CIRCLE);
		if(state == State.CROSS) Renderer.quad(buffer, x, y, 1, 1, World.CROSS);
		if(this.state.get(DELETED)) Renderer.quad(buffer, x, y, 1, 1, World.DELETED);
	}

	@Override
	public void onInteract(int mode) {
		State state = this.state.get(STATE);

		if (mode == GLFW.GLFW_PRESS && state == State.EMPTY) {
			if (this.world.circle) {
				this.state.set(STATE, State.CIRCLE);
			}else {
				this.state.set(STATE, State.CROSS);
			}

			test();

			this.world.circle = !this.world.circle;
		}
	}

	private void test() {
		boolean a = testLine(1, 0) + testLine(-1, 0) >= 4;
		boolean b = testLine(0, 1) + testLine(0, -1) >= 4;
		boolean c = testLine(1, 1) + testLine(-1, -1) >= 4;
		boolean d = testLine(1, -1) + testLine(-1, 1) >= 4;

		if(a || b || c || d) {
			Logger.info("GAME OVER! ", this.x, " ", this.y);

			this.state.set(DELETED, true);

			if(a) {markLine(1, 0); markLine(-1, 0);}
			if(b) {markLine(0, 1); markLine(0, -1);}
			if(c) {markLine(1, 1); markLine(-1, -1);}
			if(d) {markLine(1, -1); markLine(-1, 1);}
		}
	}

	private int testLine(int x, int y) {
		int c = 0;

		try {
			for (int i = 1; i < 5; i ++) {
				if (this.world.getTile(this.x + x * i, this.y + y * i) instanceof EmptyTile tile) {
					State stateThis = this.state.get(STATE);
					State stateThat = tile.state.get(STATE);

					if (stateThis == stateThat && !(boolean) tile.state.get(DELETED)) {
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

	private void markLine(int x, int y) {
		try {
			for (int i = 1; i < 5; i ++) {
				if (this.world.getTile(this.x + x * i, this.y + y * i) instanceof EmptyTile tile) {
					State stateThis = this.state.get(STATE);
					State stateThat = tile.state.get(STATE);

					if (stateThis == stateThat && !(boolean) tile.state.get(DELETED)) {
						tile.state.set(DELETED, true);
					}else{
						break;
					}
				}
			}
		}catch (Exception ignore) {

		}

	}

}
