package net.darktree.warzone.world.action.manager;

import net.darktree.warzone.Registries;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.util.ElementType;
import net.darktree.warzone.util.Registry;
import net.darktree.warzone.world.World;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

public abstract class Action {

	protected final World world;
	protected final Type type;

	protected Action(World world, Type type) {
		this.world = world;
		this.type = type;
	}

	/**
	 * The first thing called for the action, checks if the action can be applied
	 */
	protected boolean verify(Symbol symbol) {
		return true;
	}

	/**
	 * Common code, called after both redo() and undo()
	 */
	protected void common(Symbol symbol) {

	}

	/**
	 * Invoked when an action is applied, only after verify() returned true
	 */
	protected abstract void redo(Symbol symbol);

	/**
	 * Invoked when an action is undone, only after redo() has been called prior
	 */
	protected abstract void undo(Symbol symbol);

	/**
	 * Serialize action to NBT CompoundTag
	 */
	@MustBeInvokedByOverriders
	public void toNbt(CompoundTag nbt) {
		nbt.putInt("id", this.type.id());
	}

	/**
	 * Deserialize action from NBT CompoundTag
	 */
	public static Action fromNbt(CompoundTag nbt, World world) {
		return Registries.ACTIONS.byId(nbt.getInt("id")).value().create(nbt, world);
	}

	public static class Type extends ElementType<Action.Type> {
		private final Constructor constructor;

		public Type(Constructor constructor) {
			this.constructor = constructor;
		}

		public Action create(CompoundTag nbt, World world) {
			return constructor.create(world, nbt);
		}

		@Override
		public Registry<Action.Type> getRegistry() {
			return Registries.ACTIONS;
		}

		public interface Constructor {
			Action create(World world, CompoundTag nbt);
		}
	}

}
