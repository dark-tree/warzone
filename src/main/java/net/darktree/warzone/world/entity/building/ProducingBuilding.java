package net.darktree.warzone.world.entity.building;

import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.network.packet.ProductionSyncPacket;
import net.darktree.warzone.screen.ProduceScreen;
import net.darktree.warzone.screen.ScreenStack;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.building.production.ProductionState;
import net.querz.nbt.tag.CompoundTag;

public abstract class ProducingBuilding extends Building {

	protected final ProductionState production;

	public ProducingBuilding(World world, int x, int y, Building.Type type, int capacity) {
		super(world, x, y, type);
		this.production = new ProductionState(capacity, this);
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
	public void onPlayerTurnEnd(Symbol symbol) {
		if (world.canControl(tx, ty, symbol)) {
			production.apply(world.getCountry(symbol));
		}
	}

	public void sync() {
		new ProductionSyncPacket(tx, ty, production).send(world);
	}

	public void update(CompoundTag nbt) {
		production.fromNbt(nbt);
	}

}
