package net.darktree.warzone.screen;

import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.gui.Chain;
import net.darktree.warzone.client.gui.ModelBuilder;
import net.darktree.warzone.client.gui.component.UiButton;
import net.darktree.warzone.client.render.Screen;
import net.darktree.warzone.util.iterable.PagedIterable;
import net.darktree.warzone.util.math.MathHelper;

import java.util.List;

public abstract class PaginatedScreen extends Screen {

	protected int size = 1;
	protected int page = 0;
	protected int pages = 1;

	/**
	 * Configure this paginated screen
	 *
	 * @param elements the number of total entries to show
	 * @param size the number of entries per page
	 */
	protected final void setPagination(int elements, int size) {
		this.pages = (int) Math.ceil(elements / (float) size);
		this.size = size;
		this.page = MathHelper.clamp(page, 0, pages);
	}

	/**
	 * Returns an iterator to the given list
	 * that will iterate its entries on the current page
	 */
	protected final <T> PagedIterable<T> getPaged(List<T> list) {
		return PagedIterable.of(list, page, size);
	}

	/**
	 * Appends the page controls and title to the given {@link ModelBuilder}
	 */
	protected final void buildPaginatedModel(ModelBuilder builder) {

		// next page button
		builder.add(35, 1, UiButton.of(Sprites.ICON_NEXT).border(0).box(2, 2).enabled(page < (pages - 1)).react(() -> {
			page ++;
			Sounds.PAGE.play().setPitch(0.9f + MathHelper.RANDOM.nextFloat() * 0.2f);
			rebuildModel();
		}));

		// prev page button
		builder.then(Chain.BEFORE, UiButton.of(Sprites.ICON_PREV).border(0).box(2, 2).enabled(page > 0).react(() -> {
			page --;
			Sounds.PAGE.play().setPitch(0.9f + MathHelper.RANDOM.nextFloat() * 0.2f);
			rebuildModel();
		}));

	}

	protected String getPageString() {
		return TEXT_PAGE.str(page + 1, pages);
	}

}
