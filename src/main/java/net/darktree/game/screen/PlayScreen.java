package net.darktree.game.screen;

import net.darktree.Main;
import net.darktree.core.client.Colors;
import net.darktree.core.client.Sprites;
import net.darktree.core.client.render.Alignment;
import net.darktree.core.client.render.Screen;
import net.darktree.core.client.render.ScreenRenderer;
import net.darktree.core.client.window.Input;
import net.darktree.core.client.window.input.MouseButton;
import net.darktree.core.event.ClickEvent;
import net.darktree.core.world.World;
import net.darktree.core.world.WorldHolder;
import net.darktree.core.world.WorldView;
import net.darktree.core.world.entity.Entity;
import net.darktree.game.country.Country;
import net.darktree.game.country.Symbol;
import net.darktree.game.entities.UnitEntity;
import net.darktree.game.interactor.Interactor;
import net.darktree.game.interactor.UnitInteractor;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PlayScreen extends Screen {

	private Interactor interactor = null;
	private static final List<ResourceRenderer> resourceRenderers = new ArrayList<>();

	public static void setInteractor(Interactor interactor) {
		ScreenStack.asInstance(PlayScreen.class, screen -> screen.interactor = interactor);
	}

	public static void registerResourceLabel(char letter, Function<Country, Integer> provider) {
		resourceRenderers.add(country -> ScreenRenderer.text(letter + provider.apply(country).toString(), 30));
	}

	static {
		registerResourceLabel('A', country -> country.ammo);
		registerResourceLabel('Z', country -> country.armor);
//		registerResourceLabel('B', country -> 0);
//		registerResourceLabel('N', country -> 0);
//		registerResourceLabel('U', country -> 0);
//		registerResourceLabel('R', country -> 0);
	}

	private boolean isMapFocused = true;
	private final World world;

	public PlayScreen(World world) {
		this.world = world;
	}

	@Override
	public void draw(boolean focused) {

		isMapFocused = focused;

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

		// hot bar begin
		// TODO 1. split this into a separate hot bar class
		// TODO 2. The right hand side should be split off further into another separate class
		// TODO 3. MAKE IS NOT SO CURSED

		if (symbol != null) {

			ScreenRenderer.setSprite(Sprites.HOTBAR);
			ScreenRenderer.centerAt(0, -1);
//			ScreenRenderer.offset(0, 10);
			ScreenRenderer.box(630, 630, 170, 0);

			Country country = world.getCountry(symbol);

			int x = ScreenRenderer.ox;
			int y = ScreenRenderer.oy;

			ScreenRenderer.offset(30, 35);
			for (int i = 0; i < resourceRenderers.size(); i ++) {
				resourceRenderers.get(i).draw(country);
				if (i % 2 == 1) {
					ScreenRenderer.offset(90, 30);
				}else{
					ScreenRenderer.offset(0, -30);
				}
			}

			ScreenRenderer.setOffset(x + 16, y + 78);
			ScreenRenderer.setSprite(symbol.getSprite());
			ScreenRenderer.box(80, 80);

			ScreenRenderer.offset(130, 16);
			ScreenRenderer.setAlignment(Alignment.CENTER);
			ScreenRenderer.text(country.getTotalMaterials() + "", 38);

			ScreenRenderer.offset(90, 0);
			ScreenRenderer.setAlignment(Alignment.LEFT);
			ScreenRenderer.text(country.income + "", 40);

			// right section

//			ScreenRenderer.offset(385 + 74 + 4, -42 + 4);
//			ScreenRenderer.setAlignment(Alignment.RIGHT);
//			ScreenRenderer.text("<", 50);
//
//			if (ScreenRenderer.isMouseOver(58, 58)) {
//				ScreenRenderer.setColor(Colors.BUTTON_HOVER);
//			}else{
//				ScreenRenderer.setColor(Colors.OVERLAY_NONE);
//			}
//			ScreenRenderer.setSprite(Sprites.FRAME);
//			ScreenRenderer.box(58, 58);
//			ScreenRenderer.setSprite(Sprites.ICON_WALL_1);
//			ScreenRenderer.box(58, 58);
//
//			ScreenRenderer.offset(96 - 4, -4);
//			if (ScreenRenderer.isMouseOver(66, 66)) {
//				ScreenRenderer.setColor(Colors.BUTTON_HOVER);
//			}else{
//				ScreenRenderer.setColor(Colors.OVERLAY_NONE);
//			}
//			ScreenRenderer.setSprite(Sprites.FRAME);
//			ScreenRenderer.box(66, 66);
//			ScreenRenderer.setSprite(Sprites.ICON_WALL_2);
//			ScreenRenderer.box(66, 66);
//
//			ScreenRenderer.offset(96 + 4, 4);
//			if (ScreenRenderer.isMouseOver(58, 58)) {
//				ScreenRenderer.setColor(Colors.BUTTON_HOVER);
//			}else{
//				ScreenRenderer.setColor(Colors.OVERLAY_NONE);
//			}
//			ScreenRenderer.setSprite(Sprites.FRAME);
//			ScreenRenderer.box(58, 58);
//
//			ScreenRenderer.setAlignment(Alignment.LEFT);
//			ScreenRenderer.offset(50, -4);
//			ScreenRenderer.text(">", 50);
//			ScreenRenderer.offset(-385 - 74 - 96 -96 -50 - 4, 42);

			ScreenRenderer.offset(385 + 426, -42);
			ScreenRenderer.setSprite(Sprites.BUTTON_BUILDING);
			ScreenRenderer.setColor(Colors.OVERLAY_NONE);
			if (ScreenRenderer.isMouseOver(66, 66)) {
				ScreenRenderer.setColor(Colors.BUTTON_HOVER);

				if(Main.window.input().hasClicked()) {
					ScreenStack.open(new BuildScreen(world));
				}
 			}
			ScreenRenderer.box(66, 66);
			ScreenRenderer.offset(96, 0);

			ScreenRenderer.setSprite(Sprites.BUTTON_DEMOLISH);
			ScreenRenderer.setColor(Colors.OVERLAY_NONE);
			if (ScreenRenderer.isMouseOver(66, 66)) {
				ScreenRenderer.setColor(Colors.BUTTON_HOVER);
			}
			ScreenRenderer.box(66, 66);
			ScreenRenderer.offset(-426 -96, 42);

			ScreenRenderer.offset(630 / 2, 35);
//			ScreenRenderer.box(100, 100);
			ScreenRenderer.setAlignment(Alignment.CENTER);
			ScreenRenderer.text("CONSTRUCTION", 20);

			ScreenRenderer.setColor(Colors.OVERLAY_NONE);
			ScreenRenderer.setAlignment(Alignment.LEFT);

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
				ScreenStack.closeAll();
				ScreenStack.open(new PlayScreen(WorldHolder.world));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if(action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_M) {
			world.setOverlay((world, x, y, state) -> {
				if (state.getOwner() == world.getCurrentSymbol()) {
					return world.canControl(x, y) ? Colors.OVERLAY_NONE : ((Main.window.profiler.getFrameCount() / 30 % 2 == 0) ? Colors.OVERLAY_NONE : Colors.OVERLAY_FOREIGN);
				}

				return Colors.OVERLAY_FOREIGN;
			});
		}

		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_J) {
//			world.getManager().apply(new SummonAction(world, world.getCurrentSymbol(), 4, 7));
		}

		if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_B) {
			//interactor = new BuildInteractor(Tiles.BUILD, world);
			ScreenStack.open(new BuildScreen(world));
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
						if (unit.getSymbol() == world.getCurrentSymbol()) {
							interactor = new UnitInteractor(unit, world);
						}
					}else{
						world.getTileState(x, y).getTile().onInteract(world, x, y, new ClickEvent(button, action));
					}
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
