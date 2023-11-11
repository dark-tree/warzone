package net.darktree.warzone.screen;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.gui.Chain;
import net.darktree.warzone.client.gui.ModelBuilder;
import net.darktree.warzone.client.gui.component.UiIcon;
import net.darktree.warzone.client.gui.component.UiLine;
import net.darktree.warzone.client.gui.component.UiText;
import net.darktree.warzone.client.gui.prefab.GridContextFactory;
import net.darktree.warzone.client.gui.prefab.GridPrefabs;
import net.darktree.warzone.client.render.Alignment;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.client.text.Text;
import net.darktree.warzone.client.window.Input;
import net.darktree.warzone.client.window.input.MouseButton;
import net.darktree.warzone.country.Purchasable;

import java.util.List;

public abstract class ElementListScreen <T extends Purchasable> extends PaginatedScreen {

	private final List<T> elements;

	protected static final Text TEXT_COST = Text.translated("gui.cost");
	protected static final Text TEXT_MATERIALS = Text.translated("gui.materials");

	public ElementListScreen() {
		List<T> elements = getElementList();
		this.elements = elements;
		setPagination(elements.size(), 3);
	}

	@Override
	protected GridContextFactory getGridFactory() {
		return GridPrefabs.STANDARD;
	}

	protected void buildModel(ModelBuilder builder, Text title, int materials) {

		// decals
		builder.add(30, -1, UiIcon.of(Sprites.DECAL_MUG).box(10, 10)); // a bit off-screen
		builder.add(1, 9, UiIcon.of(Sprites.DECAL_MUG).box(10, 10));
		builder.add(-1, 4, UiIcon.of(Sprites.DECAL_MUG).box(10, 10));
		builder.add(25, 14, UiIcon.of(Sprites.DECAL_SPLAT).box(10, 10));

		// title
		builder.add(0, 21, UiText.of(title).box(39, 2).center());
		builder.then(Chain.BELOW, UiText.of(getPageString()).box(39, 1).center());

		// append page buttons
		buildPaginatedModel(builder);

		// top & bottom line
		builder.add(1, 19, UiLine.of(37, 0));
		builder.add(1, 4, UiLine.of(37, 0));

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
		ScreenRenderer.setAlignment(Alignment.LEFT);
		ScreenRenderer.setOffset(-540, 70);

		for (T element : getPaged(elements)) {
			drawElement(element, materials);
			ScreenRenderer.offset(0, -120);
			ScreenRenderer.setColor(Colors.NONE);
		}
	}

}
