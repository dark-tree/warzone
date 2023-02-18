package net.darktree.warzone.util.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class VennHelper {

	public static <T> List<List<T>> findAllUniquePicks(List<Set<T>> sets) {
		List<List<T>> result = new ArrayList<>();
		appendUniquePicks(sets, new ArrayList<>(), result);
		return result;
	}

	private static <T> void appendUniquePicks(List<Set<T>> sets, List<T> selected, List<List<T>> result) {
		if (selected.size() == sets.size()) {
			result.add(new ArrayList<>(selected));
			return;
		}

		for (T value : sets.get(selected.size())) {
			if (!selected.contains(value)) {
				selected.add(value);
				appendUniquePicks(sets, selected, result);
				selected.remove(selected.size() - 1);
			}
		}
	}

}
