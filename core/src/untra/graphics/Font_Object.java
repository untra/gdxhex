package untra.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Font_Object extends BitmapFont {

	public void draw(Draw_Object s_batch, java.lang.CharSequence str, float x,
			float y) {
		super.draw(s_batch, str, x, height() - y);
	}

	private float height() {
		return Gdx.graphics.getHeight();
	}

	public Font_Object(FileHandle fontFile, boolean flip) {
		super(fontFile, flip);
	}
}
