package net.darktree.warzone.screen;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.gui.Chain;
import net.darktree.warzone.client.gui.ModelBuilder;
import net.darktree.warzone.client.gui.component.UiButton;
import net.darktree.warzone.client.gui.component.UiIcon;
import net.darktree.warzone.client.gui.component.UiLine;
import net.darktree.warzone.client.gui.component.UiText;
import net.darktree.warzone.client.gui.prefab.GridContextFactory;
import net.darktree.warzone.client.gui.prefab.GridPrefabs;
import net.darktree.warzone.client.render.Screen;
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
	private static final Text TEXT_CAPACITY = Text.translated("gui.produce.capacity");

	public ProduceScreen(ProductionState state, World world, Symbol symbol) {
		this.state = state;
		this.country = world.getCountry(symbol);
		this.world = world;
	}

	@Override
	protected GridContextFactory getGridFactory() {
		return GridPrefabs.STANDARD;
	}

	@Override
	protected void buildModel(ModelBuilder builder) {

		// decals
		builder.add(25, 3, UiIcon.of(Sprites.DECAL_MUG).box(10, 10));
		builder.add(21, 0, UiIcon.of(Sprites.DECAL_MUG).box(10, 10));
		builder.add(0, 13, UiIcon.of(Sprites.DECAL_SMUDGE).box(10, 10));

		// the first recipe row, right under the header
		int row = 15;

		// title
		builder.add(0, 21, UiText.of(TEXT_TITLE).box(39, 2).center());
		builder.then(Chain.BELOW, UiText.of(TEXT_PAGE, 1, 1).box(39, 1).center());

		// table header
		builder.add(2, 17);
		builder.then(Chain.OVER, UiLine.ofRelative(35, 0));
		builder.then(Chain.OVER, UiText.of(TEXT_RESOURCE).box(13, 1).left());
		builder.then(Chain.AFTER, UiText.of(TEXT_COST).box(6, 1).left());
		builder.then(Chain.AFTER, UiText.of(TEXT_QUANTITY).box(16, 1).left());

		// append recipes
		for (Recipe recipe : state.getRecipes()) {

			Recipe.Type type = recipe.getType();

			// main row body
			builder.add(2, row);
			builder.then(Chain.OVER, UiLine.ofRelative(35, 0));
			builder.then(Chain.OVER, UiText.of(Text.translated(type.getNameKey())).box(13, 2).tint(Colors.BORDER).left());
			builder.then(Chain.AFTER, UiText.of(type.getCostString()).box(6, 2).tint(Colors.BORDER).left());
			builder.then(Chain.AFTER, UiText.of(Integer.toString(recipe.getQuantity())).box(2, 2).tint(Colors.BORDER).left());

			// increment button
			builder.then(Chain.AFTER, UiButton.of(Sprites.ICON_MINUS).border(0).box(2, 2).inset(0.4f).enabled(recipe.getQuantity() > 0).react(() -> {
				recipe.undo(country);
				state.sync();
				Sounds.DRAW_MARK.play();
				rebuildModel();
			}));

			// decrement button
			builder.then(Chain.AFTER, UiButton.of(Sprites.ICON_PLUS).border(0).box(2, 2).inset(0.4f).enabled(state.canProduce() && recipe.canProduce(country)).react(() -> {
				recipe.redo(country);
				state.sync();
				Sounds.DRAW_MARK.play();
				rebuildModel();
			}));

			row -= 2;
		}


		// capacity
		builder.add(2, 1, UiText.of(TEXT_CAPACITY, state.getCapacityString()).box(10, 2));

	}

	@Override
	public void draw(boolean focused) {
		drawBackground(Colors.SCREEN_SEPARATOR);
		drawModel();
	}

}
