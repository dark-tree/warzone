package net.darktree.game.screen.hotbar;

import net.darktree.core.client.render.Alignment;
import net.darktree.core.client.render.ScreenRenderer;
import net.darktree.core.world.World;
import net.darktree.game.country.Country;
import net.darktree.game.country.Symbol;
import net.darktree.game.screen.ResourceRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class HotbarOverview extends HotbarComponent {

	private static final List<ResourceRenderer> labels = new ArrayList<>();

	public static void registerResourceLabel(char letter, Function<Country, Integer> provider) {
		labels.add(country -> ScreenRenderer.text(letter + provider.apply(country).toString(), 30));
	}

	static {
		registerResourceLabel('A', country -> country.ammo);
		registerResourceLabel('Z', country -> country.armor);
	}

	@Override
	public void draw(boolean focused, World world, Symbol symbol) {
		Country country = world.getCountry(symbol);

		int x = ScreenRenderer.ox;
		int y = ScreenRenderer.oy;

		// render resource labels
		ScreenRenderer.offset(30, 35);
		for (int i = 0; i < labels.size(); i ++) {
			labels.get(i).draw(country);

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
	}

}
