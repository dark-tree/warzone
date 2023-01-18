package net.darktree.warzone.country;

import net.darktree.warzone.country.storage.StorageSupplier;
import net.darktree.warzone.screen.hotbar.HotbarOverview;

import java.util.Locale;

public class LabeledResource extends Resource {

	private final String label;

	public LabeledResource(String shortName, StorageSupplier storage) {
		this(shortName.toUpperCase(Locale.ROOT), shortName, storage);
	}

	public LabeledResource(String label, String shortName, StorageSupplier storage) {
		super(shortName, storage);
		this.label = label;
	}

	@Override
	public void onRegister(String id) {
		HotbarOverview.registerResourceLabel(label, this);
	}

}
