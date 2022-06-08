package net.darktree.game.buildings;

import net.darktree.core.event.ClickEvent;
import net.darktree.core.util.BuildingType;
import net.darktree.core.world.World;
import net.darktree.game.country.Symbol;
import net.darktree.game.production.ProductionState;
import net.darktree.game.screen.ProduceScreen;
import net.darktree.game.screen.ScreenStack;

public abstract class ProducingBuilding extends Building {

	protected final ProductionState production;

	public ProducingBuilding(World world, int x, int y, BuildingType type, int capacity) {
		super(world, x, y, type);
		this.production = new ProductionState(capacity);
	}

	@Override
	public void onInteract(World world, int x, int y, ClickEvent event) {
		if (event.isPressed()) {
			Symbol owner = world.getTileState(x, y).getOwner();

			if (world.canControl(x, y)) {
				ScreenStack.open(new ProduceScreen(production, world, owner));
			}
		}
	}

	@Override
	public void onPlayerTurnEnd(World world, int x, int y, Symbol symbol) {
		if (world.canControl(x, y, symbol)) {
			production.apply(world, symbol);
		}
	}

}
