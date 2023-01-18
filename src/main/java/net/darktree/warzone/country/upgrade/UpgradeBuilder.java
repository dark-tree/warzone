package net.darktree.warzone.country.upgrade;

import net.darktree.warzone.client.render.image.Sprite;

public class UpgradeBuilder {

	private final int cost;
	private final Sprite icon;

	UpgradeBuilder(int cost, Sprite icon) {
		this.cost = cost;
		this.icon = icon;
	}

	public Upgrade<Boolean> bool() {
		return new Upgrade<>(cost, icon, false, true);
	}

	public Upgrade<Integer> of(int disabled, int enabled) {
		return new Upgrade<>(cost, icon, disabled, enabled);
	}

	public Upgrade<Float> of(float disabled, float enabled) {
		return new Upgrade<>(cost, icon, disabled, enabled);
	}

}
