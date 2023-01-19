package net.darktree.warzone.client;

import net.darktree.warzone.client.render.image.Atlas;
import net.darktree.warzone.client.render.image.Sprite;
import net.darktree.warzone.client.render.image.Texture;
import net.darktree.warzone.client.render.image.UnsetTexture;
import net.darktree.warzone.util.Logger;

public class Sprites {

	public final static Atlas ATLAS = Atlas.stitchedOf("sprites");
	public final static Texture UNSET = new UnsetTexture(); // handle with care!
	public final static Sprite MISSINGNO = ATLAS.getSprite("missingno.png");
	public final static Sprite NONE = ATLAS.getSprite("none.png");

	public static Sprite getSprite(String identifier) {
		try {
			return ATLAS.getSprite(identifier);
		} catch (NullPointerException e) {
			Logger.warn("Failed to load texture '", identifier, "', using missing texture!");
			return MISSINGNO;
		}
	}

	public final static Sprite TOP = getSprite("gui/top.png");
	public final static Sprite BUILD = getSprite("gui/build_screen.png");
	public final static Sprite POPUP = getSprite("gui/small_popup.png");
	public final static Sprite HOTBAR = getSprite("gui/hotbar.png");
	public final static Sprite BUTTON_PART_LEFT = getSprite("gui/button/left.png");
	public final static Sprite BUTTON_PART_CENTER = getSprite("gui/button/center.png");
	public final static Sprite BUTTON_PART_RIGHT = getSprite("gui/button/right.png");
	public final static Sprite BUTTON_BUILDING = getSprite("gui/button/building.png");
	public final static Sprite BUTTON_DEMOLISH = getSprite("gui/button/demolish.png");
	public final static Sprite BUTTON_LEFT = getSprite("gui/button/arrow_left.png");
	public final static Sprite BUTTON_RIGHT = getSprite("gui/button/arrow_right.png");
	public final static Sprite ICON_PLUS = getSprite("gui/button/plus.png");
	public final static Sprite ICON_MINUS = getSprite("gui/button/minus.png");
	public final static Sprite FRAME = getSprite("gui/button/frame.png");
	public static final Sprite MENU_BAR = getSprite("gui/menu_bar.png");
	public static final Sprite MENU_BAR_TOP = getSprite("gui/menu_bar_top.png");

	public final static Sprite ICON_WALL_1 = getSprite("gui/wall.png");
	public final static Sprite ICON_WALL_2 = getSprite("gui/wall2.png");

	public static final Sprite UPGRADE_DOUBLE = getSprite("gui/icon/upgrade/double.png");
	public static final Sprite UPGRADE_RECYCLE = getSprite("gui/icon/upgrade/recycle.png");
	public static final Sprite UPGRADE_LOCAL = getSprite("gui/icon/upgrade/local.png");
	public static final Sprite UPGRADE_MAPS = getSprite("gui/icon/upgrade/maps.png");

	public static final Sprite BUILDING_CAPITOL = getSprite("building/capitol.png");
	public static final Sprite BUILDING_FACTORY = getSprite("building/factory.png");
	public static final Sprite BUILDING_MINE = getSprite("building/mine.png");
	public static final Sprite BUILDING_WAREHOUSE = getSprite("building/warehouse.png");
	public static final Sprite BUILDING_WAREHOUSE_LONG = getSprite("building/warehouse_long.png");
	public static final Sprite BUILDING_PARLIAMENT = getSprite("building/parliament.png");

}
