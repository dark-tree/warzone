package net.darktree.warzone.screen.hotbar;

import net.darktree.warzone.client.render.Alignment;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Resource;
import net.darktree.warzone.country.Resources;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.screen.ResourceRenderer;
import net.darktree.warzone.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HotbarOverview extends HotbarComponent {

	private static final List<ResourceRenderer> labels = new ArrayList<>();

	public static void registerResourceLabel(Resource resource) {
		labels.add(country -> ScreenRenderer.text(resource.getShortName().toUpperCase(Locale.ROOT) + country.getResource(resource).value, 30));
	}

	// TODO this is redundant now, think of a new and flexible system
	static {
		registerResourceLabel(Resources.ARMOR);
		registerResourceLabel(Resources.AMMO);
	}

	@Override
	public void draw(boolean focused, World world, Symbol symbol) {
		Country country = world.getCountry(symbol);

		ScreenRenderer.push();

		// render resource labels
		ScreenRenderer.offset(25, 35);
		for (int i = 0; i < labels.size(); i ++) {
			labels.get(i).draw(country);

			if (i % 2 == 1) {
				ScreenRenderer.offset(90, 30);
			}else{
				ScreenRenderer.offset(0, -30);
			}
		}

		ScreenRenderer.pop();
		ScreenRenderer.offset(16, 78);

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
