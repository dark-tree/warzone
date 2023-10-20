package net.darktree.warzone.screen;

import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.image.Sprite;
import net.darktree.warzone.client.text.Text;
import net.darktree.warzone.country.Purchasable;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.building.Building;

import java.util.ArrayList;
import java.util.List;

public class BuildScreen extends ElementListScreen<BuildScreen.EntryConfig> {

	private final World world;
	private final boolean play;
	private static final List<EntryConfig> entries = new ArrayList<>();

	private static final Text TEXT_TITLE = Text.translated("gui.build.title");

	public BuildScreen(World world, boolean play) {
		this.world = world;
		this.play = play;
	}

	@Override
	protected List<EntryConfig> getElementList() {
		return entries;
	}

	@Override
	protected void onElementClicked(EntryConfig element) {
		element.type.interact(world, play);
		this.close();
	}

	@Override
	public void draw(boolean focused) {
		drawTitledScreen(TEXT_TITLE, getPageString(), Sprites.BUILD, 1300, 800);
		final int materials = world.getCountry(world.getCurrentSymbol()).getTotalMaterials();
		drawElementList(materials);
	}

	public static void register(Building.Type type) {
		entries.add(new EntryConfig(type));
	}

	public static class EntryConfig implements Purchasable {
		private final Building.Type type;

		public EntryConfig(Building.Type type) {
			this.type = type;
		}

		@Override
		public String getNameKey() {
			return type.getNameKey();
		}

		@Override
		public String getDescriptionKey() {
			return type.getDescriptionKey();
		}

		@Override
		public Sprite getIcon() {
			return type.icon;
		}

		@Override
		public int getCost() {
			return type.value;
		}
	}

}
