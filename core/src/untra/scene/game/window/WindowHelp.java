package untra.scene.game.window;

import untra.graphics.Draw_Object;
import untra.graphics.GameColor;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import untra.driver.Base;

public class WindowHelp extends WindowBattle {

	private int count = 0;
	private String saved_content;
	private String parsed_text;
	private String _content;

	// private Vector2 _pos;
	public WindowHelp() {
		super(new Rectangle(0, 0, Base.window_width(), 32));
		Initialize();
	}

	public void Initialize() {
		// _pos =new Vector2((float) opos.X + 4, (float) opos.Y);
		_content = "";
		parsed_text = "";
		item_max = 0;
	}

	public void update() {
		super.update();
		if (count >= 2) {
			count--;
		} else if (count == 1) {
			count--;
			set_content(saved_content);
		}
	}

	/**
	 * Draw method
	 */
	public void draw(Draw_Object s_batch) {
		super.draw(s_batch);
		Vector2 dpos = ocoordinates();
		dpos.y -= BUFFER - 2;
		draw_text(s_batch, parsed_text, dpos, GameColor.WHITE);
	}

	/**
	 * Sets the content of the help box
	 * 
	 * @param s
	 */
	public void set_content(String s) {

		String temp = s;
		_content = temp;
		int n = s.length();
		int l = Base.window_width() / 12;
		// if the text String would normally exceed the width of the pane,
		// it inserts a new line at the start of the previous word.
		if (n > l) {
			int i = temp.lastIndexOf(' ', l);
			parsed_text = temp.substring(0, i) + '\n' + temp.substring(i);
		} else
			parsed_text = temp;
		saved_content = "";
		count = 0;
	}

	/**
	 * Sets a temporary message that will disappear in 2.5 seconds, before
	 * resetting the prior message
	 * 
	 * @param s
	 */
	public void temporary_message(String s) {
		String temp = _content;
		set_content(s);
		saved_content = temp;
		count = (int) (2.5 * Base.FrameRate);
	}
}
