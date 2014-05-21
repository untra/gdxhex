package untra.database;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import untra.driver.IXml;

public class Tile_Object implements IXml<Tile_Object>, Idata {
	public static final int DENSITY_VLOW = 5;
	public static final int DENSITY_LOW = 6;
	public static final int DENSITY_MED = 7;
	public static final int DENSITY_HIGH = 9;
	public static final int DENSITY_VHIGH = 12;

	public int id;
	public String name;
	public String passability;
	public int width;
	public int height;
	public int properties;
	public int frequency;

	public boolean is_oversized_object() {
		return (properties % 2 == 1);
	}

	public boolean is_multitile_object() {
		return (width != 1 && height != 1);
	}

	@Override
	public void xmlWrite(XmlWriter xml) throws IOException {
		xml.element("Tile_Object");
		xml.element("id").text(this.id).pop();
		xml.element("name").text(this.name).pop();
		xml.element("passability").text(this.passability).pop();
		xml.element("width").text(this.width).pop();
		xml.element("height").text(this.height).pop();
		xml.element("properties").text(this.properties).pop();
		xml.element("frequency").text(this.frequency).pop();
		xml.pop();
	}

	@Override
	public Tile_Object xmlRead(Element element) {
		Tile_Object to = new Tile_Object();
		to.id = element.getInt("id");
		to.name = element.get("name", "");
		to.passability = element.get("passability");
		to.width = element.getInt("width");
		to.height = element.getInt("height");
		to.properties = element.getInt("properties");
		to.frequency = element.getInt("frequency");
		return to;
	}

	public String toString() {
		return this.name;
	}

}
