package net.darktree.warzone.screen.menu;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.color.MutableColor;
import net.darktree.warzone.client.render.image.Sprite;
import net.darktree.warzone.util.math.MathHelper;
import net.darktree.warzone.util.math.Vec2f;

public class DecorCard {

	// TODO clean this up
	private final int x, y, w, h;
	private final Color color;
	private final float a;
	private final Sprite sprite;
	private final Vec2f w1, w2, w3, w4;
	private final Vec2f AB, BC;
	private final float dotABxAB;
	private final float dotBCxBC;

	public DecorCard(CardSource source, int w, int h, int x, int y) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.a = MathHelper.radians(MathHelper.RANDOM.nextInt(365));

		float r = MathHelper.RANDOM.nextFloat() * 0.784f;
		float g = MathHelper.RANDOM.nextFloat() * 0.392f;
		float b = MathHelper.RANDOM.nextFloat() * 0.392f;

		this.color = Color.lerp(Colors.CARD_SEPIA, MutableColor.of(r, g, b), 0.3f).alpha(0.3f).immutable();

		// corners
		this.w1 = getVecPoint(-1, +1); // A
		this.w2 = getVecPoint(-1, -1); // B
		this.w3 = getVecPoint(+1, -1); // C
		this.w4 = getVecPoint(+1, +1); // D

		this.AB = Vec2f.of(w1, w2);
		this.BC = Vec2f.of(w2, w3);

		this.dotABxAB = AB.selfDot();
		this.dotBCxBC = BC.selfDot();

		this.sprite = source.get();
	}

	/**
	 * Get the exact vertex vector for the given
	 * axis-aligned vertex coordinates
	 */
	private Vec2f getVecPoint(int px, int py) {
		final float s = MathHelper.sin(a);
		final float c = MathHelper.cos(a);
		final float w = this.w / 2.0f;
		final float h = this.h / 2.0f;

		return new Vec2f(x + px * w * c - py * h * s, y + px * w * s + py * h * c);
	}

	/**
	 * This function is analogous to:
	 * <code>Vec.dot(vec, Vec.of(x1, y1, x2, y2));</code>
	 * but avoids a temporary vector allocation
	 */
	private float quickDot(Vec2f vec, float x1, float y1, float x2, float y2) {
		return vec.x * (x2 - x1) + vec.y * (y2 - y1);
	}

	/**
	 * Draw this card in the correct orientation and position
	 */
	public void draw(int px, int py) {
		ScreenRenderer.centerAt(-1, -1);
		ScreenRenderer.setOffset(px, py);
		ScreenRenderer.setColor(color);
		ScreenRenderer.setSprite(sprite);
		ScreenRenderer.quad(w4.x, w4.y, w1.x, w1.y, w2.x, w2.y, w3.x, w3.y);
	}

	/**
	 * Check if the point (x, y) lies on this card
	 */
	public boolean has(int x, int y) {
		final float dotABxAM = quickDot(AB, w1.x, w1.y, x, y);
		final float dotBCxBM = quickDot(BC, w2.x, w2.y, x, y);

		return (0 <= dotABxAM) && (dotABxAM <= dotABxAB) && (0 <= dotBCxBM) && (dotBCxBM <= dotBCxBC);
	}

}
