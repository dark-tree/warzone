package net.darktree.warzone.country;

import net.darktree.warzone.screen.hotbar.HotbarOverview;

import java.util.Locale;

public class LabeledResource extends Resource {

	private final String label;

	public LabeledResource(String shortName, String longName) {
		this(shortName.toUpperCase(Locale.ROOT), shortName, longName);
	}

	public LabeledResource(String label, String shortName, String longName) {
		super(shortName, longName);
		this.label = label;
	}

	@Override
	public void onRegister(String id) {
		HotbarOverview.registerResourceLabel(label, this);
	}

}
