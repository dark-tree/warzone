package net.darktree.warzone.client.gui;

import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.Alignment;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.image.NinePatch;
import net.darktree.warzone.client.render.image.Sprite;
import net.darktree.warzone.util.BoundingBox;

public class DrawContext {

	public void vertex(float x, float y, float u, float v, Color color) {
		ScreenRenderer.vertex(x, y, u, v, color.r(), color.g(), color.b(), color.a());
	}

	public void drawQuad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, Sprite sprite, Color color) {
		vertex(x1, y1, sprite.u1(), sprite.v1(), color);
		vertex(x2, y2, sprite.u2(), sprite.v1(), color);
		vertex(x4, y4, sprite.u1(), sprite.v2(), color);

		vertex(x2, y2, sprite.u2(), sprite.v1(), color);
		vertex(x3, y3, sprite.u2(), sprite.v2(), color);
		vertex(x4, y4, sprite.u1(), sprite.v2(), color);
	}

	public void drawRect(float x, float y, float w, float h, Sprite sprite, Color color) {
		drawQuad(x, y, x + w, y, x + w, y + h, x, y + h, sprite, color);
	}

	public void drawLine(float x1, float y1, float x2, float y2, float w, Color color) {
		float vx = x2 - x1;
		float vy = y2 - y1;

		float len = (float) Math.sqrt(vy * vy + vx * vx) * (2 / w);
		float nx =   vy / len;
		float ny = - vx / len;

		float r1x = x1 + nx;
		float r1y = y1 + ny;
		float r2x = x1 - nx;
		float r2y = y1 - ny;
		float r3x = x2 + nx;
		float r3y = y2 + ny;
		float r4x = x2 - nx;
		float r4y = y2 - ny;

		drawQuad(r1x, r1y, r2x, r2y, r4x, r4y, r3x, r3y, Sprites.BLANK, color);
	}

	public void drawTiledLine(float x1, float y1, float x2, float y2, float w, Sprite sprite, float size, Color color) {

		// line vector
		float vx = x2 - x1;
		float vy = y2 - y1;

		w /= 2;

		// normalized line vector
		float len = (float) Math.sqrt(vy * vy + vx * vx);
		float nx = vx / len;
		float ny = vy / len;

		float px = x1;
		float py = y1;
		float step = Math.max(w, size);

		float dist = (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
		float scale = w / step;

		while (dist > 0) {
			float target = Math.min(dist, step);

			drawLine(px, py, px = px + nx * target, py = py + ny * target, w, sprite, scale, target / step, color);
			dist -= step;
		}
	}

	public void drawTiledRect(float x, float y, float w, float h, Sprite sprite, float sw, float sh, Color color) {
		float dw, dh;

		float u1 = sprite.u1();
		float v1 = sprite.v1();

		float sx = (sprite.u2() - sprite.u1()) / sw;
		float sy = (sprite.v2() - sprite.v1()) / sh;

		for (int i = 0; (dw = w - sw * i) > 0; i ++) {

			float tw = Math.min(dw, sw);
			float tx = sw * i;
			float u2 = u1 + tw * sx;

			float x1 = x + tx;
			float x2 = x + tx + tw;
			float x3 = x + tx + tw;
			float x4 = x + tx;

			for (int j = 0; (dh = h - sh * j) > 0; j ++) {

				float th = Math.min(dh, sh);
				float ty = sh * j;
				float v2 = v1 + th * sy;

				float y1 = y + ty;
				float y2 = y + ty;
				float y3 = y + ty + th;
				float y4 = y + ty + th;

				vertex(x1, y1, u1, v1, color);
				vertex(x2, y2, u2, v1, color);
				vertex(x4, y4, u1, v2, color);

				vertex(x2, y2, u2, v1, color);
				vertex(x3, y3, u2, v2, color);
				vertex(x4, y4, u1, v2, color);
			}
		}
	}

	public void drawLine(float x1, float y1, float x2, float y2, float w, Sprite sprite, float sx, float sy, Color color) {
		float vx = x2 - x1;
		float vy = y2 - y1;

		float len = (float) Math.sqrt(vy * vy + vx * vx) * (2 / w);
		float nx = vy / len;
		float ny = -vx / len;

		float r1x = x1 + nx;
		float r1y = y1 + ny;
		float r2x = x1 - nx;
		float r2y = y1 - ny;
		float r3x = x2 + nx;
		float r3y = y2 + ny;
		float r4x = x2 - nx;
		float r4y = y2 - ny;

		float u1 = sprite.u1();
		float u2 = u1 + (sprite.u2() - u1) * sx;
		float v1 = sprite.v1();
		float v2 = v1 + (sprite.v2() - v1) * sy;

//		float mu = (u1 + u2) / 2.0f;
//
//		vertex(r1x, r1y, u1, v1, edge);
//		vertex(x1, y1, mu, v1, center);
//		vertex(x2, y2, mu, v2, center);
//
//		vertex(r1x, r1y, u1, v1, edge);
//		vertex(x2, y2, mu, v2, center);
//		vertex(r3x, r3y, u1, v2, edge);
//
//		vertex(x1, y1, mu, v1, center);
//		vertex(r2x, r2y, u2, v1, edge);
//		vertex(r4x, r4y, u2, v2, edge);
//
//		vertex(x1, y1, mu, v1, center);
//		vertex(r4x, r4y, u2, v2, edge);
//		vertex(x2, y2, mu, v2, center);

		vertex(r1x, r1y, u1, v1, color);
		vertex(r2x, r2y, u2, v1, color);
		vertex(r4x, r4y, u2, v2, color);

		vertex(r4x, r4y, u2, v2, color);
		vertex(r3x, r3y, u1, v2, color);
		vertex(r1x, r1y, u1, v1, color);
	}

	public void drawDebugBox(BoundingBox box, float w, Color color) {
		drawLine(box.x1, box.y1, box.x1, box.y2, w, color);
		drawLine(box.x1, box.y2, box.x2, box.y2, w, color);
		drawLine(box.x2, box.y2, box.x2, box.y1, w, color);
		drawLine(box.x2, box.y1, box.x1, box.y1, w, color);
		drawRect(box.x1, box.y1, box.x2 - box.x1, box.y2 - box.y1, Sprites.BLANK, color.mutable().alpha(0.25f));
	}

	public void drawLineBox(BoundingBox box, float w, Sprite sprite, float size, float inset, Color color) {
		float c = w * 0.125f;

		float x1 = box.x1 + inset;
		float x2 = box.x2 - inset;
		float y1 = box.y1 + inset;
		float y2 = box.y2 - inset;

		drawTiledLine(x1, y1 - c, x1, y2 + c, w, sprite, size, color);
		drawTiledLine(x1, y2, x2, y2, w, sprite, size, color);
		drawTiledLine(x2, y2 + c, x2, y1 - c, w, sprite, size, color);
		drawTiledLine(x2, y1, x1, y1, w, sprite, size, color);
	}

	public void drawText(float x, float y, float size, Alignment alignment, String text, Color color) {
		ScreenRenderer.push();
		ScreenRenderer.setColor(color);
		ScreenRenderer.offset((int) x, (int) (y - size / 2));
		ScreenRenderer.setAlignment(alignment);
		ScreenRenderer.translatedText(size, text);
		ScreenRenderer.pop();
	}

	public void drawNinePatch(float x, float y, float w, float h, float s, boolean fill, boolean stroke, NinePatch patch, Color color) {
		float m = s / patch.edge;
		float sw = patch.width * m;
		float sh = patch.height * m;
		float se = patch.edge * m;

		if (fill) {
			drawTiledRect(x, y, w * s, h * s, patch.getSegment(1, 1), sw, sh, color);
		}

		if (stroke) {
			// edges
			drawTiledRect(x - s, y + 0, s, h * s, patch.getSegment(0, 1), se, sh, color);
			drawTiledRect(x + w * s, y + 0, s, h * s, patch.getSegment(2, 1), se, sh, color);
			drawTiledRect(x + 0, y - s, w * s, s, patch.getSegment(1, 0), sw, se, color);
			drawTiledRect(x + 0, y + h * s, w * s, s, patch.getSegment(1, 2), sw, se, color);

			// corners
			drawTiledRect(x - s, y - s, s, s, patch.getSegment(0, 0), se, se, color);
			drawTiledRect(x + w * s, y - s, s, s, patch.getSegment(2, 0), se, se, color);
			drawTiledRect(x - s, y + h * s, s, s, patch.getSegment(0, 2), se, se, color);
			drawTiledRect(x + w * s, y + h * s, s, s, patch.getSegment(2, 2), se, se, color);
		}
	}

	public boolean isDebugMode() {
		return false;
	}

}
