package net.darktree.warzone.country.upgrade;

import net.darktree.warzone.Registries;
import net.darktree.warzone.client.render.image.Sprite;
import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Purchasable;
import net.darktree.warzone.country.Resource;
import net.darktree.warzone.country.Resources;
import net.darktree.warzone.util.ElementType;
import net.darktree.warzone.util.Registry;

public class Upgrade <T> extends ElementType<Upgrade<?>> implements Purchasable {

	private final T disabled;
	private final T enabled;
	private final int cost;
	private final Sprite icon;

	public Upgrade(int cost, Sprite icon, T disabled, T enabled) {
		this.cost = cost;
		this.disabled = disabled;
		this.enabled = enabled;
		this.icon = icon;
	}

	public T get(boolean value) {
		return value ? enabled : disabled;
	}

	@Override
	public int getCost() {
		return cost;
	}

	@Override
	public String getNameKey() {
		return "upgrade." + key() + ".name";
	}

	@Override
	public String getDescriptionKey() {
		return "upgrade." + key() + ".description";
	}

	@Override
	public Sprite getIcon() {
		return icon;
	}

	@Override
	public Registry<Upgrade<?>> getRegistry() {
		return Registries.UPGRADES;
	}

	public static UpgradeBuilder create(int cost, Sprite icon) {
		return new UpgradeBuilder(cost, icon);
	}

	public boolean buy(Country country) {
		Resource.Quantified resource = Resources.MATERIALS.quantify(getCost());

		if (!country.hasResource(resource)) {
			return false;
		}

		country.removeResource(resource);
		country.upgrades.grant(this);

		return true;
	}

}
