package untra.scene.game.window;

import untra.graphics.Draw_Object;
import untra.graphics.GameColor;
import untra.graphics.Sprite;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import untra.driver.Base;

public class WindowConfirm extends WindowBattle {
	Sprite conf, canc;
	public String content;
	String[] commands = { "Yes", "No" };

	public WindowConfirm() {
		super(new Rectangle(256, 0, 288, 96));
		Initialize();
	}

	public void Initialize() {
		// opacity = 255;
		// cursor_rect_appension.Y = 32;
		cursor_rect_appension.y = 0;
		cursor_rect_appension.x = this.width() / 2;
		conf = new Sprite("Misc/Confirm.png");
		canc = new Sprite("Misc/Confirm.png");
		// this.pos.X = (Base.Window_width / 2) - (this.Width / 2);
		// this.pos.Y = (Base.Window_height / 2) - (this.Height / 2);
		this.coordinates.y = Base.window_height() - this.height();
		index = 0;
		column_max = 1;
		item_max = commands.length;
	}

	public void update() {
		super.update();
	}

	public void draw(Draw_Object s_batch) {
		super.draw(s_batch);
		// Vector2 P = new Vector2(((Width - (10 * content.Length)) / 2) - 10,
		// 0);
		Vector2 P = Vector2.Zero;
		P.add(ocoordinates());
		draw_text(s_batch, content, P, GameColor.TRUEWHITE);
		for (int i = 0; i < commands.length; i++) {
			P = new Vector2(this.width() / 2, (i * 32));
			P.add(ocoordinates());
			conf.setpos(P);
			if (i % 2 == 0)
				conf.draw(s_batch);
			else
				canc.draw(s_batch);
			P.x += 30;
			draw_text(s_batch, commands[i], P, GameColor.TRUEWHITE);
		}
	}

	@SuppressWarnings("unused")
	private int content_length() {
		return 10 * content.length();
	}

}
