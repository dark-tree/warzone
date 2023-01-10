package net.darktree.warzone.screen;

import net.darktree.warzone.Registries;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.Alignment;
import net.darktree.warzone.client.render.Screen;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.country.storage.LimitedStorageStack;
import net.darktree.warzone.country.storage.StorageNode;

public class WarehouseScreen extends Screen {

	private final LimitedStorageStack storage;

	public WarehouseScreen(LimitedStorageStack storage) {
		this.storage = storage;
	}

	private void drawTableRow(String s1, String s2, String s3) {
		ScreenRenderer.text(s1, 30);
		ScreenRenderer.offset(400, 0);
		ScreenRenderer.text(s2, 30);
		ScreenRenderer.offset(400, 0);
		ScreenRenderer.text(s3, 30);
	}

	@Override
	public void draw(boolean focused) {
		drawTitledScreen("WAREHOUSE REPORT", "PAGE 1/1", Sprites.BUILD, 1300, 800);

		ScreenRenderer.setAlignment(Alignment.LEFT);
		ScreenRenderer.setColor(Colors.BORDER);
		ScreenRenderer.offset(100, 600);

		// table heading
		ScreenRenderer.push();
		drawTableRow("RESOURCE", "QUANTITY", "CAPACITY");
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
			drawTableRow(resource.getLongName(), node.amount() + "", node.limit() + "");
			ScreenRenderer.pop();
			ScreenRenderer.offset(0, -6);
		});

		ScreenRenderer.line(0.005f, 1300 - 200, 0);

	}

}
