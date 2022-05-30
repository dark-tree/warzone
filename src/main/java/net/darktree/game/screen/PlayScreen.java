package net.darktree.game.screen;

import net.darktree.Main;
import net.darktree.core.client.Sprites;
import net.darktree.core.client.render.Screen;
import net.darktree.core.client.render.ScreenRenderer;
import net.darktree.core.client.window.Input;
import net.darktree.core.client.window.input.MouseButton;
import net.darktree.core.event.ClickEvent;
import net.darktree.core.world.World;
import net.darktree.core.world.WorldHolder;
import net.darktree.core.world.WorldView;
import net.darktree.core.world.action.SummonAction;
import net.darktree.core.world.entity.Entity;
import net.darktree.game.country.Symbol;
import net.darktree.game.entities.UnitEntity;
import net.darktree.game.interactor.BuildInteractor;
import net.darktree.game.interactor.Interactor;
import net.darktree.game.interactor.MoveInteractor;
import net.darktree.game.tiles.Tiles;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;

public class PlayScreen extends Screen {

	private Interactor interactor = null;

	public void setInteractor(Interactor interactor) {
		this.interactor = interactor;
	}

	private boolean isMapFocused = true;
	private final World world;

	public PlayScreen(World world) {
		this.world = world;
	}

	@Override
	public void draw() {

		isMapFocused = true;

		WorldHolder.draw();

		if (interactor != null) {
			interactor.draw(WorldHolder.pipeline.buffer);

			if (interactor.isClosed()) {
				interactor.close();
				interactor = null;
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

		if (interactor != null) {
			interactor.onKey(key, action, mods);
			return;
		}

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

		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_J) {
			world.getManager().apply(new SummonAction(world, world.getCurrentSymbol(), 4, 7));
		}

		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_B) {
			interactor = new BuildInteractor(Tiles.BUILD, world);
		}

		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_TAB) {
			world.nextPlayerTurn();
		}

		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_BACKSPACE) {
			world.getManager().undo(world.getCurrentSymbol());
		}
	}

	@Override
	public void onClick(int button, int action, int mods) {

		if (!isMapFocused || button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
			return;
		}

		Input input = Main.window.input();

		int x = input.getMouseMapX(world.getView());
		int y = input.getMouseMapY(world.getView());

		if (interactor != null) {
			interactor.onClick(button, action, mods, x, y);
			return;
		}

		if(button == GLFW.GLFW_MOUSE_BUTTON_1 || button == GLFW.GLFW_MOUSE_BUTTON_2) {
			if (world.isPositionValid(x, y)) {

				if (action == GLFW.GLFW_PRESS) {
					Entity entity = world.getEntity(x, y);

					if (entity instanceof UnitEntity unit) {
						if (unit.getSymbol() == world.getCurrentSymbol() && !unit.hasMoved()) {
							interactor = new MoveInteractor(unit, world);
						}
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
