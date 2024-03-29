package net.darktree.warzone.screen;

import net.darktree.warzone.Registries;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.gui.ModelBuilder;
import net.darktree.warzone.client.render.Alignment;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.client.text.Text;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.country.upgrade.Upgrade;
import net.darktree.warzone.network.packet.UpgradePacket;
import net.darktree.warzone.util.Util;
import net.darktree.warzone.world.WorldAccess;

import java.util.List;

public class ParliamentScreen extends ElementListScreen<Upgrade<?>> {

	private final WorldAccess world;
	private final Symbol symbol;

	private final static Text TEXT_ENABLED = Text.translated("gui.parliament.enabled");
	private final static Text TEXT_DISABLED = Text.translated("gui.parliament.disabled");
	private final static Text TEXT_TITLE = Text.translated("gui.parliament.title");
	private final static Text TEXT_CONFIRM = Text.translated("gui.parliament.confirm.title");
	private final static Text TEXT_BUY = Text.translated("gui.parliament.confirm.buy");

	public ParliamentScreen(WorldAccess world, Symbol symbol) {
		this.world = world;
		this.symbol = symbol;
	}

	@Override
	protected void buildModel(ModelBuilder builder) {
		buildModel(builder, TEXT_TITLE, world.getTrackingWorld().getCountry(symbol).getTotalMaterials());
	}

	@Override
	protected List<Upgrade<?>> getElementList() {
		return Util.registryAsList(Registries.UPGRADES);
	}

	@Override
	protected void onElementClicked(Upgrade<?> element) {
		ScreenStack.open(new ConfirmScreen(TEXT_CONFIRM, TEXT_BUY.str(Text.translated(element.getNameKey()))).onYes(() -> {
			new UpgradePacket(world.getCurrentSymbol(), element).sendToHost();
		}));
	}

	@Override
	protected void drawElement(Upgrade<?> element, int materials) {
		final boolean enough = element.getCost() <= materials;
		final boolean bought = world.getTrackingWorld().getCountry(world.getCurrentSymbol()).upgrades.isEnabled(element);
		final boolean self = world.getActiveSymbol() == symbol;

		checkElementInteraction(element, self && enough && !bought);

		ScreenRenderer.setAlignment(Alignment.LEFT);
		ScreenRenderer.setSprite(element.getIcon());
		ScreenRenderer.box(100, 100);
		ScreenRenderer.setColor(enough || !self || bought ? Colors.PRICE_TAG : Colors.TOO_EXPENSIVE);

		ScreenRenderer.push();
		ScreenRenderer.offset(104, 6);
		ScreenRenderer.text(30, bought ? TEXT_ENABLED : (self ? element.getCost() + "m" : TEXT_DISABLED));

		ScreenRenderer.setColor(Colors.TEXT);
		ScreenRenderer.offset(0, 54);
		ScreenRenderer.translatedText(30, element.getNameKey());
		ScreenRenderer.pop();
	}

	@Override
	public void draw(boolean focused) {
		drawBackground(Colors.SCREEN_SEPARATOR);
		drawModel();
		drawElementList(world.getTrackingWorld().getCountry(symbol).getTotalMaterials());
	}

}
