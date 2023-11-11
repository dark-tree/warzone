package net.darktree.warzone.screen;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.gui.Chain;
import net.darktree.warzone.client.gui.ModelBuilder;
import net.darktree.warzone.client.gui.component.*;
import net.darktree.warzone.client.gui.prefab.GridContextFactory;
import net.darktree.warzone.client.gui.prefab.GridPrefabs;
import net.darktree.warzone.client.text.Text;
import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.building.production.ProductionState;
import net.darktree.warzone.world.entity.building.production.Recipe;

public class ProduceScreen extends PaginatedScreen {

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
		setPagination(state.getRecipes().size(), 6);
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

		// append title and page buttons
		buildPaginatedModel(builder, TEXT_TITLE);

		// table header
		UiComposed.Builder header = UiComposed.of();
		header.add(0, 0, UiLine.ofRelative(35, 0));
		header.then(Chain.OVER, UiText.of(TEXT_RESOURCE).box(13, 1).left());
		header.then(Chain.AFTER, UiText.of(TEXT_COST).box(6, 1).left());
		header.then(Chain.AFTER, UiText.of(TEXT_QUANTITY).box(16, 1).left());
		builder.add(2, 17, header);

		// append recipes
		for (Recipe recipe : getPaged(state.getRecipes())) {

			Recipe.Type type = recipe.getType();

			// main row body
			UiComposed.Builder entry = UiComposed.of();
			entry.add(0, 0, UiLine.ofRelative(35, 0));
			entry.then(Chain.OVER, UiText.of(Text.translated(type.getNameKey())).box(13, 2).tint(Colors.BORDER).left());
			entry.then(Chain.AFTER, UiText.of(type.getCostString()).box(6, 2).tint(Colors.BORDER).left());
			entry.then(Chain.AFTER, UiText.of(Integer.toString(recipe.getQuantity())).box(2, 2).tint(Colors.BORDER).left());

			// increment button
			entry.then(Chain.AFTER, UiButton.of(Sprites.ICON_MINUS).border(0).box(2, 2).inset(0.4f).enabled(recipe.getQuantity() > 0).react(() -> {
				recipe.undo(country);
				state.sync();
				Sounds.DRAW_MARK.play();
				rebuildModel();
			}));

			// decrement button
			entry.then(Chain.AFTER, UiButton.of(Sprites.ICON_PLUS).border(0).box(2, 2).inset(0.4f).enabled(state.canProduce() && recipe.canProduce(country)).react(() -> {
				recipe.redo(country);
				state.sync();
				Sounds.DRAW_MARK.play();
				rebuildModel();
			}));

			builder.then(Chain.BELOW, entry);
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
