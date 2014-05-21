package untra.scene.game.window;

import untra.gamewindow.SelectionWindow;
import untra.graphics.Draw_Object;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import untra.database.Database;
import untra.driver.Base;

public class WindowObjectSelection extends SelectionWindow {

	public WindowObjectSelection() {
		super(new Rectangle(0, 0, Base.window_width(),
				Base.window_height() - 32));
		move_to_bottom();
		Initialize();
	}

	public void Initialize() {
		column_max = Base.window_width() / 320;
		commands = new String[Database.Tile_Object_Count()];
		denied_access = new boolean[commands.length];
		for (int i = 0; i < Database.Tile_Object_Count(); i++) {
			commands[i] = "" + i + " " + Database.tile_objects[i].name;
		}
		item_max = commands.length;

	}

	public void update() {
		super.update();
		Update_cursor_rect();
	}

	/**
	 * The draw method
	 */
	public void draw(Draw_Object s_batch) {
		super.draw(s_batch);
		drawCursorRect(s_batch);
		Vector2 p;
		textColor = NORMAL_COLOR;
		String string;
		int j;
		// note: does not use draw_item as it requires number labeling
		for (int i = getPage_item_max() * page; i < commands.length
				&& i < getPage_item_max() * (page + 1); i++) {
			j = i % getPage_item_max();
			string = commands[i];
			p = ocoordinates();
			p.x += (j / row_max()) * (width()) / column_max + 4;
			p.y += 4 + ((j % row_max()) * (32));
			s_batch.draw_regular_text(string, p.x, p.y, textColor);
		}
	}
}
