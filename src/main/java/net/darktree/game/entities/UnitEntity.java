package net.darktree.game.entities;

import net.darktree.game.country.Symbol;
import net.darktree.lt2d.graphics.vertex.Renderer;
import net.darktree.lt2d.graphics.vertex.VertexBuffer;
import net.darktree.lt2d.util.Type;
import net.darktree.lt2d.world.Pattern;
import net.darktree.lt2d.world.World;
import net.darktree.lt2d.world.entities.Entity;
import net.darktree.lt2d.world.entities.MovingEntity;

public class UnitEntity extends MovingEntity {

	private Symbol symbol = Symbol.CROSS;

	public UnitEntity(World world, int x, int y, Type<Entity> type) {
		super(world, x, y, type);
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	public void draw(VertexBuffer buffer) {
		super.draw(buffer);
		Renderer.quad(buffer, x, y, 1, 1, symbol.getSprite(), 1, 1, 1, 0);
	}

	public void colonize() {
		if (world.getTileState(tx, ty).getOwner() == this.symbol) {
			Pattern.nextColonizationPattern().iterate(world, tx, ty, pos -> {
				world.getTileState(pos.x, pos.y).setOwner(world, pos.x, pos.y, this.symbol);
			});
		}
	}

}
