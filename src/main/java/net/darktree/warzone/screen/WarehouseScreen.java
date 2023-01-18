package net.darktree.warzone.screen;

import net.darktree.warzone.Registries;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.Alignment;
import net.darktree.warzone.client.render.Screen;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.client.text.Text;
import net.darktree.warzone.country.storage.LimitedStorageStack;
import net.darktree.warzone.country.storage.StorageNode;

public class WarehouseScreen extends Screen {

	private final LimitedStorageStack storage;

	private static final Text TEXT_TITLE = Text.translated("gui.warehouse.title");
	private static final Text TEXT_RESOURCE = Text.translated("gui.warehouse.resource");
	private static final Text TEXT_QUANTITY = Text.translated("gui.warehouse.quantity");
	private static final Text TEXT_CAPACITY = Text.translated("gui.warehouse.capacity");

	public WarehouseScreen(LimitedStorageStack storage) {
		this.storage = storage;
	}

	private void drawTableRow(CharSequence a, CharSequence b, CharSequence c) {
		ScreenRenderer.literalText(30, a.toString());
		ScreenRenderer.offset(400, 0);
		ScreenRenderer.literalText(30, b.toString());
		ScreenRenderer.offset(400, 0);
		ScreenRenderer.literalText(30, c.toString());
	}

	@Override
	public void draw(boolean focused) {
		drawTitledScreen(TEXT_TITLE, TEXT_PAGE.str(1, 1), Sprites.BUILD, 1300, 800);

		ScreenRenderer.setAlignment(Alignment.LEFT);
		ScreenRenderer.setColor(Colors.BORDER);
		ScreenRenderer.offset(100, 600);

		// table heading
		ScreenRenderer.push();
		drawTableRow(TEXT_RESOURCE, TEXT_QUANTITY, TEXT_CAPACITY);
		ScreenRenderer.pop();

		ScreenRenderer.offset(0, -6);

		Registries.RESOURCES.iterate(resource -> {
			StorageNode node = storage.getResource(resource);

			if (node == null) {
				return;
			}

			ScreenRenderer.line(0.005f, 1300 - 200, 0);

			ScreenRenderer.offset(0, -40);
			ScreenRenderer.push();
			drawTableRow(Text.translated(resource.getNameKey()), node.amount() + "", node.limit() + "");
			ScreenRenderer.pop();
			ScreenRenderer.offset(0, -6);
		});

		ScreenRenderer.line(0.005f, 1300 - 200, 0);

	}

}
