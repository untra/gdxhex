package untra.scene.debug;

import untra.gamewindow.SelectionWindow;
import untra.graphics.Draw_Object;

import com.badlogic.gdx.math.Rectangle;

import untra.driver.Base;

public class DebugWindow extends SelectionWindow {
	public DebugWindow() {
		super(new Rectangle(0, 0, Base.window_width(), Base.window_height()));
	}

	public void update() {
		super.update();
	}

	public void draw(Draw_Object s_batch) {
		super.draw(s_batch);
	}
}
