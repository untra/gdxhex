package untra.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import untra.driver.Base;

public class Draw_Object extends SpriteBatch {

	private static final GameColor DEFAULT_TEXT_COLOR = GameColor.WHITE;
	private static Font_Object regular_text;
	private static Font_Object smaller_text;

	/*
	 * public void Draw_Outline(Sprite sprite, Color outline) { Color temp =
	 * (Color) sprite.getColor(); sprite.setColor(outline);
	 * sprite.setY(sprite.getY() + 1); sprite.draw(this);
	 * sprite.setY(sprite.getX() + 1); sprite.draw(this);
	 * sprite.setY(sprite.getY() - 1); sprite.draw(this);
	 * sprite.setY(sprite.getY() - 1); sprite.draw(this);
	 * sprite.setY(sprite.getX() - 1); sprite.draw(this);
	 * sprite.setY(sprite.getX() - 1); sprite.draw(this);
	 * sprite.setY(sprite.getY() + 1); sprite.draw(this);
	 * sprite.setY(sprite.getY() + 1); sprite.draw(this);
	 * sprite.setY(sprite.getX() + 1); sprite.setColor(temp); sprite.draw(this);
	 * }
	 */

	public static void init() {
		System.out.println("Drawobject Initializing...");
		//Texture.setEnforcePotImages(false);
		try {
			regular_text = new Font_Object(Gdx.files.internal("fonts/"
					+ Base.default_font_name), true);
			smaller_text = new Font_Object(Gdx.files.internal("fonts/"
					+ Base.default_font_name), true);
			regular_text.setColor(DEFAULT_TEXT_COLOR);
			smaller_text.setColor(DEFAULT_TEXT_COLOR);
			System.out.println("Drawobject Initialization Great Success!");
		} catch (Exception e) {
			System.out.println("Draw Object Initialization Failed!");
			e.printStackTrace();
		}
	}

	private float height() {
		return Gdx.graphics.getHeight();
	}

	/*
	 * // DO NOT CHANGE THIS METHOD. iT IS CONFIGURED TO FLIP DRAW CALLS FROM
	 * THE // BOTTOMLEFT TO THE TOPLEFT ORIGIN
	 * 
	 * @Override public void draw(Texture texture, float x, float y, float
	 * originX, float originY, float width, float height, float scaleX, float
	 * scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight,
	 * boolean flipX, boolean flipY) { super.draw(texture, x, height() - y,
	 * originX, originY, width, -height, scaleX, scaleY, rotation, srcX,
	 * texture.getHeight() - srcY, srcWidth, srcHeight, flipX, !flipY); }
	 * 
	 * @Override public void draw(Texture texture, float x, float y, float
	 * width, float height, int srcX, int srcY, int srcWidth, int srcHeight,
	 * boolean flipX, boolean flipY) { super.draw(texture, x, height() - y,
	 * width, -height, srcX, srcY, srcWidth, srcHeight, flipX, !flipY);
	 * 
	 * }
	 * 
	 * @Override public void draw(Texture texture, float x, float y, int srcX,
	 * int srcY, int srcWidth, int srcHeight) { this.draw(texture, x, y,
	 * texture.getWidth(), texture.getHeight(), srcX, srcY, srcWidth, srcHeight,
	 * false, false); }
	 * 
	 * @Override public void draw(Texture texture, float x, float y, float
	 * width, float height) { this.draw(texture, x, y, width, height, 0, 0,
	 * texture.getWidth(), texture.getHeight(), false, false); }
	 * 
	 * public void draw(Texture texture, float x, float y, float width, float
	 * height, boolean flipx, boolean flipy) { this.draw(texture, x, y, width,
	 * height, 0, 0, texture.getWidth(), texture.getHeight(), flipx, flipy); }
	 * 
	 * @Override public void draw(Texture texture, float x, float y, float
	 * width, float height, float u, float v, float u2, float v2) {
	 * this.draw(texture, x, y, width, height, (int) u, (int) v, (int) u2, (int)
	 * v2, false, false); }
	 * 
	 * @Override public void draw(Texture texture, float x, float y) {
	 * this.draw(texture, x, y, texture.getWidth(), texture.getHeight()); }
	 * 
	 * public void draw(Texture texture, float x, float y, boolean flipx,
	 * boolean flipy) { this.draw(texture, x, y, texture.getWidth(),
	 * texture.getHeight(), flipx, flipy); }
	 */

	public void draw(Texture texture, float x, float y, float width,
			float height, boolean flipx, boolean flipy) {
		this.draw(texture, x, y, width, height, 0, 0, texture.getWidth(),
				texture.getHeight(), flipx, flipy);
	}

	public void draw(Texture texture, float x, float y, boolean flipx,
			boolean flipy) {
		super.draw(texture, x, y, (float) texture.getWidth(),
				(float) texture.getHeight(), 0, 0, texture.getWidth(),
				texture.getHeight(), flipx, flipy);
	}

	public void draw_regular_text(java.lang.CharSequence str, float x, float y) {
		draw_regular_text(str, x, height() - y, DEFAULT_TEXT_COLOR);
	}

	public void draw_smaller_text(java.lang.CharSequence str, float x, float y) {
		// draw_smaller_text(str, x, height() - y, DEFAULT_TEXT_COLOR);
		draw_smaller_text(str, x, y, DEFAULT_TEXT_COLOR);
	}

	public void draw_regular_text(java.lang.CharSequence str, float x, float y,
			GameColor color) {
		// Color previousColor = (Color) regular_text.getColor();
		regular_text.setColor(color);
		regular_text.draw(this, str, x, height() - y);
		// regular_text.setColor(previousColor);
	}

	public void draw_smaller_text(java.lang.CharSequence str, float x, float y,
			GameColor color) {
		// GameColor previousColor = (GameColor) smaller_text.getColor();
		smaller_text.setColor(color);
		smaller_text.draw(this, str, x, height() - y);
		// smaller_text.setColor(previousColor);
	}

}
