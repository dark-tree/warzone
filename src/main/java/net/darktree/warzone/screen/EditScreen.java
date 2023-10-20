package net.darktree.warzone.screen;

import net.darktree.warzone.Registries;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.Alignment;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.client.render.image.Sprite;
import net.darktree.warzone.client.sound.SoundSystem;
import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.client.window.input.KeyEvent;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.screen.hotbar.Hotbar;
import net.darktree.warzone.screen.hotbar.HotbarComponent;
import net.darktree.warzone.screen.interactor.EntityEditInteractor;
import net.darktree.warzone.screen.interactor.OwnershipEditInteractor;
import net.darktree.warzone.screen.interactor.SurfaceEditInteractor;
import net.darktree.warzone.util.Util;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.tile.Tile;

import java.util.ArrayList;
import java.util.List;

public class EditScreen extends WorldScreen {

	private final Hotbar hotbar = new Hotbar(Sprites.HOTBAR_EDIT);
	private final List<Tool> tools = new ArrayList<>();
	int selectedToolTab;
	int time;

	public EditScreen(World world) {
		super(world);
		SoundSystem.stopMusic();

		tools.add(new Surface());
		tools.add(new Ownership());
		tools.add(new Eraser());
	}

	@Override
	public void draw(boolean focused) {
		super.draw(focused);

		hotbar.draw(focused, world, Symbol.CIRCLE);
		drawTabList(4, -5);

		buffers.draw();
	}

	private void drawTabList(int spacing, int x) {
		ScreenRenderer.centerAt(-1, 0);
		boolean wasAnySelected = false;
		final int size = 100;
		final int vertical = tools.size() * (size + 2 * spacing);
		ScreenRenderer.setOffset(0, vertical / 2);

		for (int i = 0; i < tools.size(); i ++) {
			ScreenRenderer.offset(0, -spacing);
			ScreenRenderer.offset(0, -size);

			ScreenRenderer.push();
			ScreenRenderer.offset(i == selectedToolTab ? 0 : x, 0);
			ScreenRenderer.setSprite(Sprites.LEFT_TAB);
			ScreenRenderer.box(size, size);
			if (ScreenRenderer.button(tools.get(i).sprite, size, size, true)) {
				tools.get(i).select();
			}

			if (ScreenRenderer.getLastComponent().selected) {
				if (i != selectedToolTab) {
					time = 0;
				}

				selectedToolTab = i;
				time ++;
				wasAnySelected = true;

				if (time > 90) {
					ScreenRenderer.offset(size, (size - 30) / 2);
					ScreenRenderer.text(30, tools.get(i).name);
				}
			}
			ScreenRenderer.pop();

			ScreenRenderer.setColor(Colors.NONE);
			ScreenRenderer.offset(0, -spacing);
		}

		if (!wasAnySelected) {
			selectedToolTab = -1;
			time = 0;
		}
	}

	@Override
	protected void onWorldKey(KeyEvent event) {

	}

	@Override
	protected void onWorldClick(ClickEvent event, int x, int y) {

	}

	@Override
	protected void onWorldEscape() {

	}

	public abstract class Tool extends HotbarComponent {
		public final Sprite sprite;
		public final String name;

		public Tool(Sprite sprite, String name) {
			this.sprite = sprite;
			this.name = name;
		}

		public void select() {
			hotbar.right(this);
			onBrushUpdate();
		}

		@Override
		protected String getNameKey() {
			return name;
		}

		protected abstract void onBrushUpdate();

	}

	public abstract class Brush<T> extends Tool {

		protected int size = 1;
		protected int value = 0;

		public Brush(Sprite sprite, String name) {
			super(sprite, name);
		}

		protected String getValueKey() {
			return getValue().toString();
		}

		@Override
		protected void draw(boolean focused, World world, Symbol symbol) {
			ScreenRenderer.offset(5, 15);
			ScreenRenderer.text(30, "MATERIAL:");

			ScreenRenderer.push();
			ScreenRenderer.offset(300, 0);
			if (ScreenRenderer.button(Sprites.ICON_PREV, 30, 30, value > 0)) {
				value --;
				onBrushUpdate();
			}
			ScreenRenderer.setColor(Colors.NONE);

			ScreenRenderer.offset(120, 0);
			ScreenRenderer.setAlignment(Alignment.CENTER);
			ScreenRenderer.translatedText(30, getValueKey());
			ScreenRenderer.setAlignment(Alignment.LEFT);

			ScreenRenderer.offset(120, 0);
			if (ScreenRenderer.button(Sprites.ICON_NEXT, 30, 30, value < getValues().length - 1)) {
				value ++;
				onBrushUpdate();
			}
			ScreenRenderer.setColor(Colors.NONE);

			ScreenRenderer.pop();

			ScreenRenderer.offset(0, 40);
			ScreenRenderer.text(30, "SIZE:");

			ScreenRenderer.push();
			ScreenRenderer.offset(300, 0);
			if (ScreenRenderer.button(Sprites.ICON_MINUS, 30, 30, size > 1)) {
				size --;
				onBrushUpdate();
			}
			ScreenRenderer.setColor(Colors.NONE);

			ScreenRenderer.offset(120, 0);
			ScreenRenderer.setAlignment(Alignment.CENTER);
			ScreenRenderer.text(30, "" + size);
			ScreenRenderer.setAlignment(Alignment.LEFT);

			ScreenRenderer.offset(120, 0);
			if (ScreenRenderer.button(Sprites.ICON_PLUS, 30, 30, size < 10)) {
				size ++;
				onBrushUpdate();
			}
			ScreenRenderer.setColor(Colors.NONE);

			ScreenRenderer.pop();
		}

		protected T getValue() {
			return getValues()[value];
		}

		protected abstract T[] getValues();

	}

	public class Ownership extends Brush<Symbol> {

		public Ownership() {
			super(Sprites.TOOL_BORDER, "OWNERSHIP BRUSH");
		}

		@Override
		protected void onBrushUpdate() {
			WorldScreen.setInteractor(new OwnershipEditInteractor(getValue(), world, size));
		}

		@Override
		protected Symbol[] getValues() {
			return Symbol.values();
		}

	}

	public class Surface extends Brush<Tile> {

		public Surface() {
			super(Sprites.TOOL_SURFACE, "SURFACE BRUSH");
		}

		@Override
		protected void onBrushUpdate() {
			WorldScreen.setInteractor(new SurfaceEditInteractor(getValue(), world, size));
		}

		@Override
		protected Tile[] getValues() {
			return Util.registryAsList(Registries.TILES).toArray(Tile[]::new);
		}

		@Override
		protected String getValueKey() {
			return getValue().getNameKey();
		}
	}

	public class Eraser extends Tool {

		public Eraser() {
			super(Sprites.TOOL_ERASE, "OBJECT ERASER");
		}

		@Override
		protected void onBrushUpdate() {
			WorldScreen.setInteractor(new EntityEditInteractor(null, entity -> {}, world));
		}

		@Override
		protected void draw(boolean focused, World world, Symbol symbol) {

		}

	}

}
