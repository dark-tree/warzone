package net.darktree.warzone.client;

import net.darktree.warzone.client.render.image.*;

public class Sprites {

	public static Atlas ATLAS = Atlas.stitchedOf("sprites");
	public final static Texture UNSET = new UnsetTexture(); // handle with care!
	public final static SpriteLoader LOADER = new SpriteLoader();

	public static Sprite getSprite(String identifier) {
		return LOADER.getSprite(identifier);
	}

	public static NinePatch getNinePatch(String identifier) {
		return LOADER.getNinePatch(identifier);
	}

	public final static Sprite MISSINGNO = getSprite("missingno.png");
	public final static Sprite NONE = getSprite("none.png");
	public final static Sprite BLANK = getSprite("blank.png");

	public final static Sprite TOP = getSprite("gui/top.png");
	public final static Sprite BUILD = getSprite("gui/build_screen.png");
	public final static Sprite POPUP = getSprite("gui/small_popup.png");
	public final static Sprite PAUSE = getSprite("gui/pause_screen.png");
	public final static Sprite HOTBAR_PLAY = getSprite("gui/hotbar_play.png");
	public final static Sprite HOTBAR_EDIT = getSprite("gui/hotbar_edit.png");
	public final static Sprite BUTTON_PART_LEFT = getSprite("gui/button/left.png");
	public final static Sprite BUTTON_PART_CENTER = getSprite("gui/button/center.png");
	public final static Sprite BUTTON_PART_RIGHT = getSprite("gui/button/right.png");
	public final static Sprite BUTTON_BUILDING = getSprite("gui/button/building.png");
	public final static Sprite BUTTON_DEMOLISH = getSprite("gui/button/demolish.png");
	public final static Sprite BUTTON_LEFT = getSprite("gui/button/arrow_left.png");
	public final static Sprite BUTTON_RIGHT = getSprite("gui/button/arrow_right.png");
	public final static Sprite ICON_PLUS = getSprite("gui/button/plus.png");
	public final static Sprite ICON_MINUS = getSprite("gui/button/minus.png");
	public final static Sprite ICON_NEXT = getSprite("gui/button/next.png");
	public final static Sprite ICON_PREV = getSprite("gui/button/prev.png");
	public final static Sprite FRAME = getSprite("gui/button/frame.png");
	public static final Sprite MENU_BAR = getSprite("gui/menu_bar.png");
	public static final Sprite MENU_BAR_TOP = getSprite("gui/menu_bar_top.png");
	public static final Sprite LEFT_TAB = getSprite("gui/left_tab.png");

	public final static Sprite ICON_WALL_1 = getSprite("gui/wall.png");
	public final static Sprite ICON_WALL_2 = getSprite("gui/wall2.png");

	public static final Sprite UPGRADE_DOUBLE = getSprite("gui/icon/upgrade/double.png");
	public static final Sprite UPGRADE_RECYCLE = getSprite("gui/icon/upgrade/recycle.png");
	public static final Sprite UPGRADE_LOCAL = getSprite("gui/icon/upgrade/local.png");
	public static final Sprite UPGRADE_MAPS = getSprite("gui/icon/upgrade/maps.png");
	public static final Sprite UPGRADE_BRIDGE = getSprite("gui/icon/upgrade/bridge.png");

	public static final Sprite TOOL_SURFACE = getSprite("gui/icon/tool_surface.png");
	public static final Sprite TOOL_BORDER = getSprite("gui/icon/tool_border.png");
	public static final Sprite TOOL_ERASE = getSprite("gui/icon/tool_entity_erase.png");

	public static final Sprite BUILDING_CAPITOL = getSprite("building/capitol.png");
	public static final Sprite BUILDING_FACTORY = getSprite("building/factory.png");
	public static final Sprite BUILDING_MINE = getSprite("building/mine.png");
	public static final Sprite BUILDING_WAREHOUSE = getSprite("building/warehouse.png");
	public static final Sprite BUILDING_WAREHOUSE_LONG = getSprite("building/warehouse_long.png");
	public static final Sprite BUILDING_PARLIAMENT = getSprite("building/parliament.png");

	public static final Sprite STRUCTURE_FENCE = getSprite("building/fence.png");
	public static final Sprite STRUCTURE_WALL = getSprite("building/wall.png");
	public static final Sprite STRUCTURE_BRIDGE = getSprite("building/bridge.png");

	public static final Sprite LINE_OVERLAY = getSprite("gui/grid/component/lines.png");
	public static final Sprite BUTTON_HOVER = getSprite("gui/grid/component/hover.png");
	public static final Sprite BUTTON_PRESSED = getSprite("gui/grid/component/pressed.png");
	public static final Sprite BUTTON_DISABLED = getSprite("gui/grid/component/disabled.png");

	public static final NinePatch GRID_NINE_PATCH = getNinePatch("gui/grid/background");

	static {
		LOADER.load(ATLAS);
	}

}
