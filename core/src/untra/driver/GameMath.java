package untra.driver;

import java.io.IOException;
import java.util.ArrayList;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlWriter;

import untra.database.IId;

public class GameMath {

	/**
	 * Returns an expanded rectangle from the given coordinates
	 * 
	 * @param r
	 *            rectangle to be expanded
	 * @param x
	 *            width to expand by (both sides)
	 * @param y
	 *            height to expand by (both sides)
	 * @return An inflated Rectangle
	 */
	public static Rectangle inflateRectangle(Rectangle r, float x, float y) {
		r.x -= x;
		r.y -= y;
		r.width += (2 * x);
		r.height += (2 * y);
		return r;
	}

	public static Rectangle newinflatedRectangle(Rectangle r, float x, float y) {
		Rectangle pRectangle = new Rectangle(r.x, r.y, r.width, r.height);
		return inflateRectangle(pRectangle, x, y);
	}

	public static Vector2 locationPoint(Rectangle r) {
		return new Vector2((int) r.x, (int) r.y);
	}

	public static boolean inRectangle(Rectangle r, Vector2 p) {
		return r.contains(p);
	}

	/**
	 * returns true if a corner of rectangle b exists somewhere in rectangle a
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean intersects(Rectangle a, Rectangle b) {
		Vector2 p = new Vector2((int) b.x, (int) b.y);
		if (inRectangle(a, p))
			return true;
		p = new Vector2((int) (b.x + b.width), (int) b.y);
		if (inRectangle(a, p))
			return true;
		p = new Vector2((int) (b.x + b.width), (int) (b.y + b.height));
		if (inRectangle(a, p))
			return true;
		p = new Vector2((int) (b.x), (int) (b.y + b.height));
		if (inRectangle(a, p))
			return true;
		return false;
	}

	public void xmlWriteArrayListIds(XmlWriter xml, ArrayList<IId> list)
			throws IOException {
		// TODO
	}
}
