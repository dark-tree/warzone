package net.darktree.warzone.screen;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.Alignment;
import net.darktree.warzone.client.render.Screen;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.client.text.Text;
import net.darktree.warzone.client.window.Input;
import net.darktree.warzone.client.window.input.MouseButton;
import net.darktree.warzone.country.Purchasable;

import java.util.List;

public abstract class ElementListScreen <T extends Purchasable> extends Screen {

	private final List<T> elements;

	protected int page;
	protected final int pages;

	private static final Text TEXT_COST = Text.translated("gui.cost");
	private static final Text TEXT_MATERIALS = Text.translated("gui.materials");

	public ElementListScreen() {
		final List<T> elements = getElementList();
		this.page = 0;
		this.pages = (int) Math.ceil(elements.size() / 3.0f);
		this.elements = elements;
	}

	protected abstract List<T> getElementList();
	protected abstract void onElementClicked(T element);

	protected void drawElementDescription(T element) {
		text(-45, 130, TEXT_COST.str(Text.translated(element.getDescriptionKey()), element.getCost() + "m"), Alignment.LEFT);
	}

	protected void checkElementInteraction(T element, boolean active) {
		if (ScreenRenderer.isMouseOver(400, 100)) {
			ScreenRenderer.setColor(Colors.BUTTON_HOVER);
			drawElementDescription(element);
			Input input = Main.window.input();

			if (input.isButtonPressed(MouseButton.LEFT)) {
				ScreenRenderer.setColor(Colors.BUTTON_PRESSED);
			}

			if (active && input.hasClicked()) {
				onElementClicked(element);
			}
		}
	}

	protected void drawElement(T element, int materials) {
		final boolean enough = element.getCost() <= materials;
		checkElementInteraction(element, enough);

		ScreenRenderer.setAlignment(Alignment.LEFT);
		ScreenRenderer.setSprite(element.getIcon());
		ScreenRenderer.box(100, 100);

		ScreenRenderer.setColor(enough ? Colors.PRICE_TAG : Colors.TOO_EXPENSIVE);

		ScreenRenderer.push();
		ScreenRenderer.offset(104, 6);
		ScreenRenderer.text(30, element.getCost() + "m");
		ScreenRenderer.setColor(Colors.NONE);
		ScreenRenderer.offset(0, 54);
		ScreenRenderer.translatedText(30, element.getNameKey());
		ScreenRenderer.pop();
	}

	protected void drawElementList(int materials) {
		final int start = this.page * 3;
		final int end = Math.min(start + 3, elements.size());

		ScreenRenderer.offset(100, 100);
		ScreenRenderer.setAlignment(Alignment.LEFT);
		ScreenRenderer.text(30, TEXT_MATERIALS.str(materials));
		ScreenRenderer.setOffset(-540, 70);

		for (int i = start; i < end; i ++) {
			drawElement(elements.get(i), materials);
			ScreenRenderer.offset(0, -120);
			ScreenRenderer.setColor(Colors.NONE);
		}

		ScreenRenderer.setOffset(650 - 250, -400 + 100);
		if (ScreenRenderer.button(Sprites.BUTTON_LEFT, 64, 64, page > 0)) {
			this.page --;
			Sounds.PAGE.play();
		}

		ScreenRenderer.offset(70, 0);
		if (ScreenRenderer.button(Sprites.BUTTON_RIGHT, 64, 64, page < (pages - 1))) {
			this.page ++;
			Sounds.PAGE.play();
		}
	}

	protected String getPageString() {
		return TEXT_PAGE.str(page + 1, pages);
	}

}
