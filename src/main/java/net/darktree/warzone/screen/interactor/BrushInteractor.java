package net.darktree.warzone.screen.interactor;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.render.image.Sprite;
import net.darktree.warzone.client.render.vertex.Renderer;
import net.darktree.warzone.client.render.vertex.VertexBuffer;
import net.darktree.warzone.client.window.Input;
import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.client.window.input.MouseButton;
import net.darktree.warzone.util.math.MathHelper;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.tile.TilePos;

import java.util.function.Consumer;

public abstract class BrushInteractor<T> extends Interactor {

	protected final World world;
	protected final T target;
	protected final int radius;
	private int frame;
	private int x, y;

	public BrushInteractor(T target, World world, int radius) {
		this.target = target;
		this.world = world;
		this.radius = radius - 1;
	}

	protected abstract void place(T material, int x, int y, boolean erase);
	protected abstract Sprite sprite(T material);

	@Override
	public void draw(VertexBuffer texture, VertexBuffer color) {
		Input input = Main.window.input();
		int x = input.getMouseTileX(world.getView());
		int y = input.getMouseTileY(world.getView());

		if (!world.isPositionValid(x, y)) {
			return;
		}

		float wave = (float) (Math.sin(frame / 6f - Math.PI/2) + 1) / 3f;
		around(x, y, pos -> {
			Renderer.quad(texture, pos.x, pos.y, 1, 1, sprite(target), 0, 1, 0, 0.3f * wave);
		});

		if (this.x != x || this.y != y) {
			this.x = x;
			this.y = y;
			boolean erase = input.isButtonPressed(MouseButton.RIGHT);

			if (erase || input.isButtonPressed(MouseButton.LEFT)) {
				around(x, y, pos -> place(target, pos.x, pos.y, erase));
			}
		}

		frame ++;
	}

	@Override
	public void onClick(ClickEvent event, int x, int y) {
		if (world.isPositionValid(x, y) && event.isPressed()) {
			boolean erase = event.isRightClick();
			around(x, y, pos -> place(target, pos.x, pos.y, erase));
		}
	}

	private void around(int x, int y, Consumer<TilePos> consumer) {
		for (int ox = -radius; ox <= radius; ox ++) {
			for (int oy = -radius; oy <= radius; oy ++) {
				int tx = ox + x, ty = oy + y;

				if (verify(tx, ty) && MathHelper.getStandardDistance(x, y, tx, ty) <= radius) {
					consumer.accept(new TilePos(tx, ty));
				}
			}
		}
	}

	protected boolean verify(int x, int y) {
		return world.isPositionValid(x, y) && world.getEntity(x, y) == null;
	}

}
