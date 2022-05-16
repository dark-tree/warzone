package net.darktree.core.client.window;

import net.darktree.Main;
import net.darktree.core.client.window.input.MouseButton;
import net.darktree.core.event.ClickEvent;
import net.darktree.core.world.World;
import net.darktree.core.world.entity.MovingEntity;
import net.darktree.core.world.overlay.PathfinderOverlay;
import net.darktree.core.world.path.Path;
import net.darktree.core.world.path.Pathfinder;
import net.darktree.game.country.Symbol;
import net.darktree.game.entities.UnitEntity;
import net.darktree.game.tiles.Tiles;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;

public class Input {

	private final Window window;

	private float prevX;
	private float prevY;
	private float zoomMin;
	private float zoomMax;

	// TODO make not public (maybe)
	public float offsetX;
	public float offsetY;
	public float scaleX;
	public float scaleY;
	public float guiScale = 1;
	public float zoom = 0.1f;

	public Input(Window window) {
		this.window = window;
	}

	public void setZoomRange(float min, float max) {
		this.zoomMin = min;
		this.zoomMax = max;
	}

	public void setGuiScale(float scale) {
		this.guiScale = scale;
	}

	void keyHandle(long handle, int key, int scancode, int action, int mods) {
		if(action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_S) {
			CompoundTag tag = new CompoundTag();
			Main.world.toNbt(tag);
			try {
				NBTUtil.write(tag, "./map.dat", true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if(action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_L) {
			try {
				World.load((CompoundTag) NBTUtil.read("./map.dat", true).getTag());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if(action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_M) {
			Main.world.setOverlay((world, x, y, state, color) -> {
				if (state.getOwner() != Symbol.NONE) {
					color.set(1, 1, 1);
				}else{
					color.set(0, 0, 0);
				}
			});
		}

		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_K) {
			if (this.entity instanceof UnitEntity unit) {
				unit.colonize();
			}
		}

		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_J) {
			Main.world.placeBuilding(1, 1, Tiles.BUILD);
		}

		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_X) {
			if (this.entity instanceof UnitEntity unit) {
				if (unit.getSymbol() == Main.world.getCurrentSymbol()) {
					unit.revert();
				}
			}
		}

		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_TAB) {
			Main.world.nextPlayerTurn();
		}
	}

	void cursorHandle(long handle, double x, double y) {
		if (isButtonPressed(MouseButton.MIDDLE)) {
			var ox = (prevX - x) / window.width() * -2 / scaleX;
			var oy = (prevY - y) / window.height() * 2 / scaleY;

			offsetX += ox;
			offsetY += oy;
		}

		prevX = (float) x;
		prevY = (float) y;
	}

	/**
	 * Just for shits and giggles (and testing), pathfinding ofc won't be done here later on
	 * TODO: Remove this
	 */
	private Pathfinder pathfinder = null;
	private MovingEntity entity = null;

	void pathfinderBegin(int x, int y) {
		pathfinder = new Pathfinder(Main.world, x, y, 5, Main.world.getCurrentSymbol());
		Main.world.setOverlay(new PathfinderOverlay(pathfinder));
	}

	void pathfinderApply(MovingEntity entity, int x, int y) {
		if (pathfinder != null) {
			if (pathfinder.canReach(x, y)) {
				Path path = pathfinder.getPathTo(x, y);
				entity.follow(path);
			}
		}
	}

	void clickHandle(long handle, int button, int action, int mods) {
		ClickEvent event = new ClickEvent(button, action);

		int x = getMouseMapX();
		int y = getMouseMapY();

		if(button == GLFW.GLFW_MOUSE_BUTTON_1 || button == GLFW.GLFW_MOUSE_BUTTON_2) {
			if (Main.world.isPositionValid(x, y)) {

				if (action == GLFW.GLFW_PRESS && isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
					if (pathfinder == null) {
						entity = (MovingEntity) Main.world.getEntity(x, y);

						if (entity instanceof UnitEntity unit) {
							if (unit.getSymbol() != Main.world.getCurrentSymbol()) {
								return;
							}
						}

						if (entity != null) {
							pathfinderBegin(x, y);
						}
					}else{

						if (entity instanceof UnitEntity unit) {
							if (unit.getSymbol() != Main.world.getCurrentSymbol()) {
								return;
							}
						}

						pathfinderApply(entity, x, y);
						pathfinder = null;
						entity = null;
						Main.world.setOverlay(null);
					}
				}else{
					Main.world.getTileState(x, y).getTile().onInteract(Main.world, x, y, event);
				}

			}
		}
	}

	void updateScale() {
		scaleX = zoom * window.height() / (float) window.width();
		scaleY = zoom;
	}

	void scrollHandle(long handle, double x, double y) {
		zoom += (float) (zoom * y * 0.15f);

		if (zoom < zoomMin) zoom = zoomMin;
		if (zoom > zoomMax) zoom = zoomMax;

		updateScale();
	}

	public float getMouseScreenX() {
		return (prevX / window.width() * 2 - 1);
	}

	public float getMouseScreenY() {
		return (prevY / window.height() * -2 + 1);
	}

	public int getMouseMapX() {
		return (int) (getMouseScreenX() / scaleX - offsetX);
	}

	public int getMouseMapY() {
		return (int) (getMouseScreenY() / scaleY - offsetY);
	}

	public boolean isKeyPressed(int key) {
		return GLFW.glfwGetKey(window.handle, key) == GLFW.GLFW_PRESS;
	}

	public boolean isButtonPressed(MouseButton button) {
		return GLFW.glfwGetMouseButton(window.handle, button.code) == GLFW.GLFW_PRESS;
	}

}
