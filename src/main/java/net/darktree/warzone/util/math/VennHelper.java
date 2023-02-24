package net.darktree.warzone.util.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class VennHelper {

	/**
	 * Returns a list of unique set selections so that no selection is used in more than one set,
	 * if no solution exists an empty list will be returned. If any of the input sets is empty
	 * no solution can ever exist, so an empty set will always be returned in that case.
	 * <pre>{@code
	 *     List<Set<Integer>> sets = new ArrayList<>();
	 *     sets.add(new HashSet<>(Arrays.asList(1, 2, 3));
	 *     sets.add(new HashSet<>(Arrays.asList(2)));
	 *     sets.add(new HashSet<>(Arrays.asList(2, 3)));
	 *
	 *     List<List<Integer>> solutions = findAllUniquePicks(sets);
	 *     // solutions will equal [[1, 2, 3]] (single solution)
	 *     // 1 corresponds to the first set [1, 2, 3]
	 *     // 2 to the second set [2]
	 *     // and 3 to the third, [2, 3]
	 * }</pre>
	 *
	 * @param sets a list of sets
	 * @return a list of solutions, each solution contains X unique entries (where X is the number of sets in the input)
	 */
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
