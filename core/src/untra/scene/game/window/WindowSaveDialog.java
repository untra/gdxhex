package untra.scene.game.window;

import untra.gamewindow.TextInputWindow;
import untra.graphics.Draw_Object;

import java.io.IOException;
import java.io.StringWriter;

import untra.scene.game.map.HexTable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlWriter;

import untra.driver.Base;
import untra.driver.Input;

/**
 * The WindowSaveDialog prompts the user to save the current map being worked
 * on.
 * 
 * @author samuel
 * 
 */
public class WindowSaveDialog extends TextInputWindow {

	private String text = "";
	private String userinputString = "";
	private HexTable maparray;

	public WindowSaveDialog(HexTable maparray) {
		super(new Rectangle(Base.window_width() / 4,
				Base.window_height() / 2 - 48, Base.window_width() / 2, 96));
		move_to_bottom();
		this.maparray = maparray;
	}

	@Override
	public void input(String text) {
		super.input(text);
		userinputString = text;
		this.text = "Map saved as \n    " + text;
		active = true;
		// map saving
		// indicates the user has canceled the saveas
		if (text == "") {
			canceled();
			return;
		}
		maparray.setName(text);
		StringWriter stringWriter = new StringWriter();
		XmlWriter writer = new XmlWriter(stringWriter);
		FileHandle handle = Gdx.files.local("src/Content/Data/Map Data/" + text
				+ ".xml");
		try {
			handle.file().createNewFile();
			maparray.xmlWrite(writer);
			handle.writeString(stringWriter.toString(), false);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println("Map saved to " + handle.path());
	}

	public void update() {
		super.update();
		if (!active)
			return;
		if (Input.triggerA())
			active = false;
	}

	@Override
	public void canceled() {
		super.canceled();
		this.text = "Map not saved!";
		this.userinputString = "";
		active = true;

	}

	@Override
	public String getUserInput() {
		return userinputString;
	}

	public void draw(Draw_Object s_batch) {
		super.draw(s_batch);
		Vector2 p = ocoordinates();
		s_batch.draw_smaller_text(text, p.x, p.y);
	}
}
