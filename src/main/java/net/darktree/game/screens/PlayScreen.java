package net.darktree.game.screens;

import net.darktree.Main;
import net.darktree.core.client.Sprites;
import net.darktree.core.client.render.Screen;
import net.darktree.core.client.render.ScreenRenderer;
import net.darktree.core.client.window.Input;
import net.darktree.core.client.window.input.MouseButton;
import net.darktree.core.event.ClickEvent;
import net.darktree.core.world.Pattern;
import net.darktree.core.world.World;
import net.darktree.core.world.WorldHolder;
import net.darktree.core.world.WorldView;
import net.darktree.core.world.entity.MovingEntity;
import net.darktree.core.world.overlay.PathfinderOverlay;
import net.darktree.core.world.path.Path;
import net.darktree.core.world.path.Pathfinder;
import net.darktree.core.world.task.BuildTask;
import net.darktree.core.world.task.ColonizeTask;
import net.darktree.core.world.task.MoveTask;
import net.darktree.game.country.Symbol;
import net.darktree.game.entities.UnitEntity;
import net.darktree.game.tiles.Tiles;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;

public class PlayScreen extends Screen {

	private boolean isMapFocused = true;
	private final World world;

	public PlayScreen(World world) {
		this.world = world;
	}

	@Override
	public void draw() {

		isMapFocused = true;

		WorldHolder.draw();

		if (pathfinder != null) {
			int x = Main.window.input().getMouseMapX(world.getView());
			int y = Main.window.input().getMouseMapY(world.getView());

			if (pathfinder.canReach(x, y)) {
				pathfinder.getPathTo(x, y).draw(WorldHolder.pipeline.buffer);
			}
		}

		WorldHolder.pipeline.flush();

		// render hud
		Symbol symbol = world.getCurrentSymbol();

		ScreenRenderer.setSprite(Sprites.TOP);

		ScreenRenderer.centerAt(0, 1);
		ScreenRenderer.offset(-240, -120);
		if (ScreenRenderer.box(480, 120)) {
			isMapFocused = false;
		}

		ScreenRenderer.offset(300, 20);
		if (ScreenRenderer.button("END", 2, 38, 80)) {
			world.nextPlayerTurn();
		}

		if (symbol != null) {
			ScreenRenderer.setSprite(symbol.getSprite());
			ScreenRenderer.setOffset(-220, -98);
			ScreenRenderer.box(80, 80);
		}

		ScreenRenderer.centerAt(-1, 1);
		ScreenRenderer.setOffset(0, -40);
		ScreenRenderer.text(Main.window.profiler.getFrameRate() + " FPS", 30);

		if (symbol != null) {
			ScreenRenderer.centerAt(-1, -1);
			ScreenRenderer.setOffset(0, 35);
			ScreenRenderer.text(symbol + "\n" + world.getCountry(symbol).getTotalMaterials() + "m", 30);
		}
	}

	@Override
	public void onKey(int key, int action, int mods) {

		if(action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_S) {
			CompoundTag tag = new CompoundTag();
			world.toNbt(tag);
			try {
				NBTUtil.write(tag, "./map.dat", true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if(action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_L) {
			try {
				World.load((CompoundTag) NBTUtil.read("./map.dat", true).getTag());
				Main.screens.clear();
				Main.screens.push(new PlayScreen(WorldHolder.world));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if(action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_M) {
			world.setOverlay((world, x, y, state, color) -> {
				if (state.getOwner() != Symbol.NONE) {
					color.a = 0;
				}else{
					color.set(0, 0, 0);
				}
			});
		}

		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_K) {
			if (this.entity instanceof UnitEntity unit) {
				world.getManager().apply(world.getCurrentSymbol(), new ColonizeTask(unit));
			}
		}

		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_J) {
			world.getManager().apply(world.getCurrentSymbol(), new BuildTask(Tiles.BUILD, 2, 2));
		}

		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_TAB) {
			world.nextPlayerTurn();
		}

		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_BACKSPACE) {
			world.getManager().undo(world.getCurrentSymbol());
		}
	}

	/**
	 * TODO: Cleanup
	 */
	private Pathfinder pathfinder = null;
	private MovingEntity entity = null;

	void pathfinderBegin(int x, int y) {
		pathfinder = new Pathfinder(world, x, y, 5, world.getCurrentSymbol(), Pattern.IDENTITY);
		world.setOverlay(new PathfinderOverlay(pathfinder));
	}

	void pathfinderApply(MovingEntity entity, int x, int y) {
		if (pathfinder != null) {
			if (pathfinder.canReach(x, y)) {
				Path path = pathfinder.getPathTo(x, y);
				world.getManager().apply(world.getCurrentSymbol(), new MoveTask(entity, path));
			}
		}
	}

	@Override
	public void onClick(int button, int action, int mods) {

		if (!isMapFocused) {
			return;
		}

		Input input = Main.window.input();

		int x = input.getMouseMapX(world.getView());
		int y = input.getMouseMapY(world.getView());

		if(button == GLFW.GLFW_MOUSE_BUTTON_1 || button == GLFW.GLFW_MOUSE_BUTTON_2) {
			if (world.isPositionValid(x, y)) {

				if (action == GLFW.GLFW_PRESS && input.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
					if (pathfinder == null) {
						entity = (MovingEntity) world.getEntity(x, y);

						if (entity instanceof UnitEntity unit) {
							if (unit.getSymbol() != world.getCurrentSymbol()) {
								return;
							}
						}

						if (entity != null && !entity.hasMoved()) {
							pathfinderBegin(x, y);
						}
					}else{

						if (entity instanceof UnitEntity unit) {
							if (unit.getSymbol() != world.getCurrentSymbol()) {
								return;
							}
						}

						pathfinderApply(entity, x, y);
						pathfinder = null;
						entity = null;
						world.setOverlay(null);
					}
				}else{
					world.getTileState(x, y).getTile().onInteract(world, x, y, new ClickEvent(button, action));
				}

			}
		}
	}

	@Override
	public void onResize(int w, int h) {
		WorldView view = world.getView();

		view.setZoom(view.zoom);
	}

	@Override
	public void onScroll(float value) {
		WorldView view = world.getView();

		float zoom = view.zoom * (1 + value * 0.15f);

		if (zoom < Input.MAP_ZOOM_MIN) zoom = Input.MAP_ZOOM_MIN;
		if (zoom > Input.MAP_ZOOM_MAX) zoom = Input.MAP_ZOOM_MAX;

		view.setZoom(zoom);
	}

	public void onMove(float x, float y) {
		Input input = Main.window.input();

		if (input.isButtonPressed(MouseButton.MIDDLE)) {
			float ox = (input.prevX - x) / Main.window.width() * -2;
			float oy = (input.prevY - y) / Main.window.height() * 2;

			world.getView().drag(ox, oy);
		}
	}

}
