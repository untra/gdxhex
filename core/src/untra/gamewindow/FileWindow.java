package untra.gamewindow;

import untra.graphics.Draw_Object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import untra.driver.Base;

public class FileWindow extends SelectionWindow {

	private FileHandle[] files;

	public FileWindow(String path) {
		super(new Rectangle(0, 0, Base.window_width(),
				Base.window_height() - 32));
		move_to_bottom();
		Initialize(path);
	}

	public void Initialize(String path) {
		column_max = Base.window_width() / 320;
		files = Gdx.files.local("src/Content/Data/" + path).list();
		cursor_rect = new Rectangle(0, 0, 320, 24);
		commands = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			commands[i] = "" + i + " " + files[i].nameWithoutExtension();
		}
		item_max = commands.length;
		index = 0;
	}

	public void update() {
		super.update();
		Update_cursor_rect();
	}

	/**
	 * returns the filehandle at the specified index. If the index is out of
	 * bounds or encounters any errors, it returns null.
	 * 
	 * @return
	 */
	public FileHandle fileAtIndex() {
		try {
			return files[index];
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void renameAtIndex() {
		// TODO complete this
	}

	public void deleteAtIndex() {
		try {
			files[index].delete();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public void draw(Draw_Object s_batch) {
		super.draw(s_batch);
		drawCursorRect(s_batch);
		Vector2 p;
		textColor = NORMAL_COLOR;
		String string;
		int j;
		for (int i = getPage_item_max() * page; i < commands.length
				&& i < getPage_item_max(); i++) {
			j = i % getPage_item_max();
			string = commands[i];
			p = ocoordinates();
			p.x += (j / row_max()) * (width()) / column_max + 4;
			p.y += 4 + ((j % row_max()) * (32));
			s_batch.draw_regular_text(string, p.x, p.y, textColor);
		}
	}

}
