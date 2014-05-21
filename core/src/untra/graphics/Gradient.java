package untra.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class Gradient extends Sprite {

	public boolean is_vertical;
	public Color Color1;
	public Color Color2;
	protected Pixmap colormap;

	// / <summary>
	// / Creates a rectangular gradient w wide by h tall
	// / </summary>
	// / <param name="w">width</param>
	// / <param name="h">height</param>
	// / <param name="a">color a</param>
	// / <param name="b">color b</param>
	// / <param name="vertical">true if vertical gradient, false if
	// horizontal</param>
	public Gradient(int w, int h, Color a, Color b, boolean vertical,
			boolean bordered) {
		super("Windowskin/gradient.png");
		_width = w;
		_height = h;
		Color1 = a;
		Color2 = b;
		is_vertical = vertical;
		colormap = new Pixmap(w, h, Pixmap.Format.Alpha);
		if (bordered)
			add_borders();
		else {
			colormap = reset_colors(colormap);
			super.texture = new Texture(colormap);
		}
		// src_rect(new Rectangle(pos.X, (int) pos.Y, image.Width,
		// image.Height));
	}

	public Gradient(int w, int h, Color a, Color b) {
		this(w, h, a, b, false, false);
	}

	private Pixmap reset_colors(Pixmap colormap, int a, int b) {
		float o = Color1.a;
		float r;
		Color c;
		if (is_vertical)
			for (int y = a; y < (height() - a); y++) {
				r = ((float) y / (float) (height() - 1));
				c = (Color) Color1.lerp(Color2, r).cpy();
				c.a = o;
				colormap.setColor(c);
				colormap.fillRectangle(b, y, (int) width() - b, 1);
			}
		else
			for (int x = b; x < (width() - b); x++) {
				r = ((float) x / (float) (width() - 1));
				c = (Color) Color1.lerp(Color2, r);
				c.a = 0;
				colormap.setColor(c);
				colormap.fillRectangle(x, a, 1, (int) height() - a);
			}
		return colormap;
	}

	private Pixmap reset_colors(Pixmap colormap) {
		return reset_colors(colormap, 0, 0);

	}

	public void add_borders() {
		_width += 4;
		_height += 4;
		Pixmap tempmap = new Pixmap(_width, _height, Pixmap.Format.RGBA8888);
		tempmap.setColor(Color.WHITE);
		tempmap.fillRectangle(0, 0, _width, _height);
		tempmap.setColor(Color.BLACK);
		tempmap.fillRectangle(1, 1, _width - 2, _height - 2);
		tempmap.drawPixmap(colormap, 2, 2);
		colormap = reset_colors(tempmap, 2, 2);
		super.texture = new Texture(colormap);
	}

	public void update() {
		super.update();
	}

	public void draw(Draw_Object s_batch) {
		update();
		super.draw(s_batch);
	}
}
