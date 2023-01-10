package net.darktree.warzone.screen;

import net.darktree.warzone.Registries;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.Alignment;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.country.upgrade.Upgrade;
import net.darktree.warzone.network.packet.UpgradePacket;
import net.darktree.warzone.util.Util;
import net.darktree.warzone.world.World;

import java.util.List;

public class ParliamentScreen extends ElementListScreen<Upgrade<?>> {

	private final World world;
	private final Symbol symbol;

	public ParliamentScreen(World world, Symbol symbol) {
		this.world = world;
		this.symbol = symbol;
	}

	@Override
	protected List<Upgrade<?>> getElementList() {
		return Util.registryAsList(Registries.UPGRADES);
	}

	@Override
	protected void onElementClicked(Upgrade<?> element) {
		ScreenStack.open(new ConfirmScreen("ARE YOU SURE?", "[BUY " + element.getName() + "?]", confirmed -> {
			if (confirmed) new UpgradePacket(world.getCurrentSymbol(), element).sendToHost();
		}));
	}

	@Override
	protected void drawElement(Upgrade<?> element, int materials) {
		final boolean enough = element.getCost() <= materials;
		final boolean bought = world.getCountry(world.getCurrentSymbol()).upgrades.isEnabled(element);
		final boolean self = world.getActiveSymbol() == symbol;

		checkElementInteraction(element, self && enough && !bought);

		ScreenRenderer.setAlignment(Alignment.LEFT);
		ScreenRenderer.setSprite(element.getIcon());
		ScreenRenderer.box(100, 100);
		ScreenRenderer.setColor(enough || !self ? Colors.PRICE_TAG : Colors.TOO_EXPENSIVE);

		ScreenRenderer.push();
		ScreenRenderer.offset(104, 6);
		ScreenRenderer.text(bought ? "ENABLED" : (self ? element.getCost() + "m" : "DISABLED"), 30);

		ScreenRenderer.setColor(Colors.NONE);
		ScreenRenderer.offset(0, 54);
		ScreenRenderer.text(element.getName(), 30);
		ScreenRenderer.pop();
	}

	@Override
	public void draw(boolean focused) {
		drawTitledScreen("PARLIAMENTARY UPGRADES", getPageString(), Sprites.BUILD, 1300, 800);
		final int materials = world.getCountry(symbol).getTotalMaterials();
		drawElementList(materials);
	}

}
