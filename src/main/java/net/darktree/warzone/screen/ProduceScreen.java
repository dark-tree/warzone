package net.darktree.warzone.screen;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
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

	public ProduceScreen(ProductionState state, World world, Symbol symbol) {
		this.state = state;
		this.country = world.getCountry(symbol);
	}

	@Override
	public void draw(boolean focused) {

		ScreenRenderer.centerAt(-1, -1);
		ScreenRenderer.setSprite(Sprites.NONE);
		ScreenRenderer.setColor(Colors.SCREEN_SEPARATOR);
		ScreenRenderer.box(Main.window.width() * 2, Main.window.height() * 2);

		ScreenRenderer.centerAt(0, 0);
		ScreenRenderer.setColor(Colors.NONE);
		ScreenRenderer.setSprite(Sprites.BUILD);

		text(0, 310, "SELECT A RESOURCE TO PRODUCE IN THIS FACTORY", Alignment.CENTER);
		text(0, 270, "PAGE 1/1", Alignment.CENTER);
		box(-650, -400, 1300, 800);

		ScreenRenderer.push();
		ScreenRenderer.setAlignment(Alignment.LEFT);
		ScreenRenderer.setColor(Colors.BORDER);
		ScreenRenderer.offset(100, 600);

		// table heading
		ScreenRenderer.push();
		ScreenRenderer.text("RESOURCE", 30);
		ScreenRenderer.offset(400, 0);
		ScreenRenderer.text("COST", 30);
		ScreenRenderer.offset(200, 0);
		ScreenRenderer.text("QUANTITY", 30);
		ScreenRenderer.pop();

		ScreenRenderer.offset(0, -6);

		for (Recipe recipe : state.getRecipes()) {
			ScreenRenderer.line(0.005f, 1300 - 200, 0);

			Recipe.Type type = recipe.getType();
			ScreenRenderer.offset(0, -40);
			ScreenRenderer.push();
			ScreenRenderer.text(type.getNameString(), 30);
			ScreenRenderer.offset(400, 0);
			ScreenRenderer.text(type.getCostString(), 30);
			ScreenRenderer.offset(200, 0);
			ScreenRenderer.text("" + recipe.getQuantity(), 30);

			ScreenRenderer.offset(60, 0);
			if (ScreenRenderer.button(Sprites.ICON_MINUS, 35, 35, recipe.getQuantity() > 0)) {
				recipe.undo(country);
			}

			ScreenRenderer.offset(60, 0);
			if (ScreenRenderer.button(Sprites.ICON_PLUS, 35, 35, state.canProduce() && recipe.canProduce(country))) {
				recipe.redo(country);
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
