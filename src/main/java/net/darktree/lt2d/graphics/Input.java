package net.darktree.lt2d.graphics;

import net.darktree.Main;
import net.darktree.game.country.Symbol;
import net.darktree.game.entities.UnitEntity;
import net.darktree.game.tiles.Tiles;
import net.darktree.lt2d.world.Pattern;
import net.darktree.lt2d.world.World;
import net.darktree.lt2d.world.entities.MovingEntity;
import net.darktree.lt2d.world.overlay.PathfinderOverlay;
import net.darktree.lt2d.world.path.Path;
import net.darktree.lt2d.world.path.Pathfinder;
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
	public float zoom = 0.1f;
	public float scaleX;
	public float scaleY;
	public float guiScale = 1;

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
				if (state.getOwner().symbol != Symbol.NONE) {
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
	}

	void cursorHandle(long handle, double x, double y) {
		if (this.isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_3)) {
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
		pathfinder = new Pathfinder(Main.world, x, y, 5);
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
		int x = (int) ((prevX / window.width() * 2 - 1) / scaleX - offsetX);
		int y = (int) ((prevY / window.height() * -2 + 1) / scaleY - offsetY);

		if(button == GLFW.GLFW_MOUSE_BUTTON_1 || button == GLFW.GLFW_MOUSE_BUTTON_2) {
			if (Main.world.isPositionValid(x, y)) {

				if (action == GLFW.GLFW_PRESS && isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
					if (pathfinder == null) {
						entity = (MovingEntity) Main.world.getEntity(x, y);

						if (entity != null) {
							pathfinderBegin(x, y);
						}
					}else{
						pathfinderApply(entity, x, y);
						pathfinder = null;
						entity = null;
						Main.world.setOverlay(null);
					}
				}else{
					// TODO: map GLFW event to some event class/enum, don't pass raw 'action' as-is
					Main.world.getTileState(x, y).getTile().onInteract(Main.world, x, y, action);
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

	public boolean isKeyPressed(int key) {
		return GLFW.glfwGetKey(window.handle, key) == GLFW.GLFW_PRESS;
	}

	public boolean isButtonPressed(int button) {
		return GLFW.glfwGetMouseButton(window.handle, button) == GLFW.GLFW_PRESS;
	}
}
