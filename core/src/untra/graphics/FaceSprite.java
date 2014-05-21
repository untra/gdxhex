package untra.graphics;

import com.badlogic.gdx.math.MathUtils;

import untra.driver.Base;

public class FaceSprite extends SpriteSet {
	private static final int FRAME_INTERVAL = 4;
	private static final int BLINK_LOWER_BOUND = 24;
	private static final int BLINK_UPPER_BOUND = 96;
	private int blink_random = 48;
	private boolean _is_blinking;

	public boolean is_blinking() {
		return _is_blinking;
	}

	private void set_blinking(boolean value) {
		if (_is_blinking && value == false)
			update_blink();
		_is_blinking = value;
	}

	// SpriteSet face;
	public boolean is_talk;
	public boolean eyes_closed;

	public FaceSprite(String face) {
		super("Sprites/Faces/" + face, 4, 1);
	}

	private void update_blink() {
		blink_random = MathUtils.random(BLINK_LOWER_BOUND, BLINK_UPPER_BOUND);
	}

	public void draw(Draw_Object s_batch) {
		update();
		super.draw(s_batch);
	}

	public void update() {
		super.update();
		byte x = 0;
		if (eyes_closed) {
			x++;
			set_blinking(true);
		} else if (Base.ticks() / FRAME_INTERVAL % BLINK_UPPER_BOUND == blink_random) {
			x++;
			set_blinking(true);
		} else {
			set_blinking(false);
		}
		if (is_talk && Base.ticks() / FRAME_INTERVAL % 2 == 0) {
			x++;
		}
		X_index = x;
	}
}
