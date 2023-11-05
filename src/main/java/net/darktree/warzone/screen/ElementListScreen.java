package net.darktree.warzone.screen;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.gui.Chain;
import net.darktree.warzone.client.gui.GridContext;
import net.darktree.warzone.client.gui.ModelBuilder;
import net.darktree.warzone.client.gui.component.UiButton;
import net.darktree.warzone.client.gui.component.UiLine;
import net.darktree.warzone.client.gui.component.UiText;
import net.darktree.warzone.client.gui.prefab.GridContextFactory;
import net.darktree.warzone.client.render.Alignment;
import net.darktree.warzone.client.render.Screen;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.client.text.Text;
import net.darktree.warzone.client.window.Input;
import net.darktree.warzone.client.window.input.MouseButton;
import net.darktree.warzone.country.Purchasable;
import net.darktree.warzone.util.math.MathHelper;

import java.util.List;

public abstract class ElementListScreen <T extends Purchasable> extends Screen {

	private final List<T> elements;

	protected int page;
	protected final int pages;

	protected static final Text TEXT_COST = Text.translated("gui.cost");
	protected static final Text TEXT_MATERIALS = Text.translated("gui.materials");

	public ElementListScreen() {
		final List<T> elements = getElementList();
		this.page = 0;
		this.pages = (int) Math.ceil(elements.size() / 3.0f);
		this.elements = elements;
	}

	@Override
	protected GridContextFactory getGridFactory() {
		return () -> new GridContext(39, 23);
	}

	protected void buildModel(ModelBuilder builder, Text title, int materials) {
		// title
		builder.add(0, 21, UiText.of(title).box(39, 2).center());
		builder.then(Chain.BELOW, UiText.of(getPageString()).box(39, 1).center());

		// next page button
		builder.add(35, 1, UiButton.of(Sprites.ICON_NEXT).disable().border(0).box(2, 2).enabled(page < (pages - 1)).react(() -> {
			page ++;
			Sounds.PAGE.play().setPitch(0.9f + MathHelper.RANDOM.nextFloat() * 0.2f);
			rebuildModel();
		}));

		// prev page button
		builder.then(Chain.BEFORE, UiButton.of(Sprites.ICON_PREV).disable().border(0).box(2, 2).enabled(page > 0).react(() -> {
			page --;
			Sounds.PAGE.play().setPitch(0.9f + MathHelper.RANDOM.nextFloat() * 0.2f);
			rebuildModel();
		}));

		// top & bottom line
		builder.add(1, 19, UiLine.of(38, 19));
		builder.add(1, 4, UiLine.of(38, 4));

		// materials
		builder.add(2, 1, UiText.of(TEXT_MATERIALS, materials).box(10, 2));
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
		ScreenRenderer.setColor(Colors.TEXT);
		ScreenRenderer.offset(0, 54);
		ScreenRenderer.translatedText(30, element.getNameKey());
		ScreenRenderer.pop();
	}

	protected void drawElementList(int materials) {
		final int start = this.page * 3;
		final int end = Math.min(start + 3, elements.size());

		ScreenRenderer.offset(100, 100);
		ScreenRenderer.setAlignment(Alignment.LEFT);
//		ScreenRenderer.text(30, TEXT_MATERIALS.str(materials));
		ScreenRenderer.setOffset(-540, 70);

		for (int i = start; i < end; i ++) {
			drawElement(elements.get(i), materials);
			ScreenRenderer.offset(0, -120);
			ScreenRenderer.setColor(Colors.NONE);
		}
	}

	protected String getPageString() {
		return TEXT_PAGE.str(page + 1, pages);
	}

}
