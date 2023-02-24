package net.darktree.warzone.world.action;

import net.darktree.warzone.Registries;
import net.darktree.warzone.world.action.manager.Action;

public class Actions {
	public static final Action.Type BUILD = Registries.ACTIONS.register("build", new Action.Type(BuildAction::new));
	public static final Action.Type COLONIZE = Registries.ACTIONS.register("colonize", new Action.Type(ColonizeAction::new));
	public static final Action.Type DECONSTRUCT = Registries.ACTIONS.register("deconstruct", new Action.Type(DeconstructBuildingAction::new));
	public static final Action.Type ENTITY_SHOT = Registries.ACTIONS.register("unit_shot", new Action.Type(EntityShotAction::new));
	public static final Action.Type ENTITY_MOVE = Registries.ACTIONS.register("unit_move", new Action.Type(MoveUnitAction::new));
	public static final Action.Type SUMMON_UNIT = Registries.ACTIONS.register("unit_summon", new Action.Type(SummonAction::new));
	public static final Action.Type TOGGLE_ARMOR = Registries.ACTIONS.register("toggle_armor", new Action.Type(ToggleArmorAction::new));
	public static final Action.Type BUILD_MINE = Registries.ACTIONS.register("build_mine", new Action.Type(BuildMineAction::new));
	public static final Action.Type BUILD_BRIDGE = Registries.ACTIONS.register("build_bridge", new Action.Type(BuildBridgeAction::new));
}
