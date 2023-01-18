package net.darktree.warzone.screen;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.Alignment;
import net.darktree.warzone.client.render.Screen;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.client.text.Text;
import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.building.production.ProductionState;
import net.darktree.warzone.world.entity.building.production.Recipe;

public class ProduceScreen extends Screen {

	private final ProductionState state;
	private final Country country;
	private final World world;

	private static final Text TEXT_RESOURCE = Text.translated("gui.produce.resource");
	private static final Text TEXT_COST = Text.translated("gui.produce.cost");
	private static final Text TEXT_QUANTITY = Text.translated("gui.produce.quantity");
	private static final Text TEXT_TITLE = Text.translated("gui.produce.title");

	public ProduceScreen(ProductionState state, World world, Symbol symbol) {
		this.state = state;
		this.country = world.getCountry(symbol);
		this.world = world;
	}

	private void drawTableRow(CharSequence a, CharSequence b, CharSequence c) {
		ScreenRenderer.literalText(30, a.toString());
		ScreenRenderer.offset(400, 0);
		ScreenRenderer.literalText(30, b.toString());
		ScreenRenderer.offset(200, 0);
		ScreenRenderer.literalText(30, c.toString());
	}

	@Override
	public void draw(boolean focused) {

		final boolean active = world.getActiveSymbol() == country.symbol;
		drawTitledScreen(TEXT_TITLE.str(), TEXT_PAGE.str(1, 1), Sprites.BUILD, 1300, 800);

		ScreenRenderer.push();
		ScreenRenderer.setAlignment(Alignment.LEFT);
		ScreenRenderer.setColor(Colors.BORDER);
		ScreenRenderer.offset(100, 600);

		// table heading
		ScreenRenderer.push();
		drawTableRow(TEXT_RESOURCE, TEXT_COST, TEXT_QUANTITY);
		ScreenRenderer.pop();

		ScreenRenderer.offset(0, -6);

		for (Recipe recipe : state.getRecipes()) {
			ScreenRenderer.line(0.005f, 1300 - 200, 0);

			Recipe.Type type = recipe.getType();
			ScreenRenderer.offset(0, -40);
			ScreenRenderer.push();
			drawTableRow(Text.translated(type.getNameKey()), type.getCostString(), "" + recipe.getQuantity());

			ScreenRenderer.offset(60, 0);
			if (active && ScreenRenderer.button(Sprites.ICON_MINUS, 35, 35, recipe.getQuantity() > 0)) {
				recipe.undo(country);
				state.sync();
				Sounds.DRAW_MARK.play();
			}

			ScreenRenderer.offset(60, 0);
			if (active && ScreenRenderer.button(Sprites.ICON_PLUS, 35, 35, state.canProduce() && recipe.canProduce(country))) {
				recipe.redo(country);
				state.sync();
				Sounds.DRAW_MARK.play();
			}

			ScreenRenderer.pop();
			ScreenRenderer.offset(0, -6);
			ScreenRenderer.setColor(Colors.BORDER);
		}

		ScreenRenderer.line(0.005f, 1300 - 200, 0);
		ScreenRenderer.pop();

		ScreenRenderer.offset(100, 100);
		ScreenRenderer.translatedText(30, "gui.produce.capacity", state.getCapacityString());

		ScreenRenderer.setColor(Colors.NONE);

	}

}
