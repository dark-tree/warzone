package net.darktree.warzone.screen;

import net.darktree.warzone.Registries;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.gui.Chain;
import net.darktree.warzone.client.gui.ModelBuilder;
import net.darktree.warzone.client.gui.component.UiComposed;
import net.darktree.warzone.client.gui.component.UiIcon;
import net.darktree.warzone.client.gui.component.UiLine;
import net.darktree.warzone.client.gui.component.UiText;
import net.darktree.warzone.client.gui.prefab.GridContextFactory;
import net.darktree.warzone.client.gui.prefab.GridPrefabs;
import net.darktree.warzone.client.render.Screen;
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

	@Override
	protected GridContextFactory getGridFactory() {
		return GridPrefabs.STANDARD;
	}

	@Override
	protected void buildModel(ModelBuilder builder) {

		// decals
		builder.add(30, -1, UiIcon.of(Sprites.DECAL_BLOB).box(10, 10)); // a bit off-screen
		builder.add(1, 12, UiIcon.of(Sprites.DECAL_MUG).box(10, 10));
		builder.add(0, 4, UiIcon.of(Sprites.DECAL_SPLAT).box(10, 10));

		// title
		builder.add(0, 21, UiText.of(TEXT_TITLE).box(39, 2).center());
		builder.then(Chain.BELOW, UiText.of(TEXT_PAGE, 1, 1).box(39, 1).center());

		// table header
		UiComposed.Builder header = UiComposed.of();
		header.add(0, 0, UiLine.ofRelative(35, 0));
		header.then(Chain.OVER, UiText.of(TEXT_RESOURCE).box(13, 1).left());
		header.then(Chain.AFTER, UiText.of(TEXT_QUANTITY).box(12, 1).left());
		header.then(Chain.AFTER, UiText.of(TEXT_CAPACITY).box(10, 1).left());

		builder.add(2, 17, header);

		// append rows
		Registries.RESOURCES.iterate(resource -> {
			StorageNode node = storage.getResource(resource);

			if (node == null) {
				return;
			}

			UiComposed.Builder entry = UiComposed.of();
			entry.add(0, 0, UiLine.ofRelative(35, 0));
			entry.then(Chain.OVER, UiText.of(Text.translated(resource.getNameKey())).box(13, 2).tint(Colors.BORDER).left());
			entry.then(Chain.AFTER, UiText.of(Integer.toString(node.amount())).box(12, 2).tint(Colors.BORDER).left());
			entry.then(Chain.AFTER, UiText.of(Integer.toString(node.limit())).box(10, 2).tint(Colors.BORDER).left());

			builder.then(Chain.BELOW, entry);

		});

	}

	@Override
	public void draw(boolean focused) {
		drawBackground(Colors.SCREEN_SEPARATOR);
		drawModel();
	}

}
