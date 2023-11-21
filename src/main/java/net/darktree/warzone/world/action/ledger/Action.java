package net.darktree.warzone.world.action.ledger;

import net.darktree.warzone.Registries;
import net.darktree.warzone.util.ElementType;
import net.darktree.warzone.util.Registry;
import net.darktree.warzone.world.WorldSnapshot;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class Action {

	protected final Action.Type type;

	public Action(Type type) {
		this.type = type;
	}

	public boolean isRevertible() {
		return true;
	}

	public boolean isToggleable(Action previous) {
		return false;
	}

	public boolean apply(WorldSnapshot world, boolean animated) {
		return true;
	}

	@OverridingMethodsMustInvokeSuper
	public void toNbt(CompoundTag nbt) {
		nbt.putString("id", type.key());
	}

	public static Action load(@NotNull CompoundTag nbt) {
		return Registries.ACTIONS.byKey(nbt.getString("id")).value().create(nbt);
	}

	public static class Type extends ElementType<Action.Type> {
		private final Action.Type.Constructor constructor;

		public Type(Action.Type.Constructor constructor) {
			this.constructor = constructor;
		}

		public Action create(CompoundTag nbt) {
			return constructor.create(nbt);
		}

		@Override
		public Registry<Action.Type> getRegistry() {
			return Registries.ACTIONS;
		}

		public interface Constructor {
			Action create(CompoundTag nbt);
		}
	}

}
