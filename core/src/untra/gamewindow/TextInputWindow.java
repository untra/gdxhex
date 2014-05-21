package untra.gamewindow;

import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.math.Rectangle;

import untra.driver.Base;

public class TextInputWindow extends Window implements TextInputListener {

	public TextInputWindow(Rectangle rect) {
		super(rect);
	}

	public TextInputWindow() {
		super(new Rectangle(Base.window_width() / 4,
				Base.window_height() / 2 - 48, Base.window_width() / 2, 96));
		move_to_bottom();
	}

	@Override
	public void input(String text) {
	}

	@Override
	public void canceled() {
	}

	public String getUserInput() {
		return "";
	}

}
