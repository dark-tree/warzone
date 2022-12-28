package net.darktree.warzone.screen;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.Alignment;
import net.darktree.warzone.client.render.Screen;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.building.production.ProductionState;
import net.darktree.warzone.world.entity.building.production.Recipe;
import org.lwjgl.glfw.GLFW;

public class ProduceScreen extends Screen {

	private final ProductionState state;
	private final Country country;
	private final World world;

	public ProduceScreen(ProductionState state, World world, Symbol symbol) {
		this.state = state;
		this.country = world.getCountry(symbol);
		this.world = world;
	}

	private void drawTableRow(String s1, String s2, String s3) {
		ScreenRenderer.text(s1, 30);
		ScreenRenderer.offset(400, 0);
		ScreenRenderer.text(s2, 30);
		ScreenRenderer.offset(200, 0);
		ScreenRenderer.text(s3, 30);
	}

	@Override
	public void draw(boolean focused) {

		final boolean active = world.getActiveSymbol() == country.symbol;
		drawTitledScreen("SELECT A RESOURCE TO PRODUCE IN THIS FACTORY", "PAGE 1/1", Sprites.BUILD, 1300, 800);

		ScreenRenderer.push();
		ScreenRenderer.setAlignment(Alignment.LEFT);
		ScreenRenderer.setColor(Colors.BORDER);
		ScreenRenderer.offset(100, 600);

		// table heading
		ScreenRenderer.push();
		drawTableRow("RESOURCE", "COST", "QUANTITY");
		ScreenRenderer.pop();

		ScreenRenderer.offset(0, -6);

		for (Recipe recipe : state.getRecipes()) {
			ScreenRenderer.line(0.005f, 1300 - 200, 0);

			Recipe.Type type = recipe.getType();
			ScreenRenderer.offset(0, -40);
			ScreenRenderer.push();
			drawTableRow(type.getNameString(), type.getCostString(), "" + recipe.getQuantity());

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
		ScreenRenderer.text("CAPACITY: " + state.getCapacityString(), 30);

		ScreenRenderer.setColor(Colors.NONE);

	}

	@Override
	public void onKey(int key, int action, int mods) {
		super.onKey(key, action, mods);

		if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
			this.close();
		}
	}
}
